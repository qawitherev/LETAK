package com.abing.letak.ordernowactivity.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.abing.letak.MainMenuActivity
import com.abing.letak.R
import com.abing.letak.databinding.FragmentBookingConfirmedBinding
import com.abing.letak.needassistance.NeedAssistanceActivity
import com.abing.letak.ordernowactivity.TimerService
import com.abing.letak.ordernowactivity.viewmodels.TimeLeftViewModel
import com.abing.letak.viewmodel.UserBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.api.LogDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.log
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class BookingConfirmedFragment : Fragment() {
    private lateinit var bookingRef: DocumentReference
    private var _binding: FragmentBookingConfirmedBinding? = null
    private val binding get() = _binding!!
    private val userBookingViewModel: UserBookingViewModel by activityViewModels()
    private lateinit var userRef: DocumentReference
    private val timeLeftViewModel: TimeLeftViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private val userIdViewModel: UserIdViewModel by viewModels()
    private val TAG = "BookingConfirmedFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingConfirmedBinding.inflate(layoutInflater)

        initialSetup()
        changeNavigationBarColour()
        binding.parkButton.setOnClickListener { startParking() }
        settingUpTimer()
        return binding.root
    }

    private fun initialSetup() {
        bookingRef = db.collection("users").document(userIdViewModel.userId).collection("bookings")
            .document(userBookingViewModel.bookingId.value.toString())
        userRef = db.collection("users").document(userIdViewModel.userId)
    }

    private fun settingUpTimer() {
        timeLeftViewModel.startTimer()
        timeLeftViewModel.elapsedTime.observe(viewLifecycleOwner) { time ->
            binding.timeLeft.text = time
        }
    }

    private fun startParking() {
        updateLotOccupied(userBookingViewModel.lotId.value.toString())
        timeLeftViewModel.stopTimer()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentDateTime = LocalDateTime.now().format(dateTimeFormatter)
        userBookingViewModel.setParkingStart(currentDateTime)
        calculateParkingEnd()
        updateParkingStartEndFirestore()
        updateOrderNowStatus()
        updateActiveBookingId()
        val intent = Intent(requireContext(), MainMenuActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun updateLotOccupied(lotId: String) {
        Log.d(TAG, "updateLotOccupied: $lotId")
        val lotRef = db.collection("parkingLots").document(lotId)
        val increment = FieldValue.increment(1)
        lotRef.update("lotOccupied", increment)
    }

    private fun updateActiveBookingId() {
        userRef.update("activeBookingId", userBookingViewModel.bookingId.value.toString())
    }

    private fun updateOrderNowStatus() {
        val userRef = db.collection("users").document(userIdViewModel.userId)
        userRef.update("orderNowStatus", true)
    }

    private fun calculateParkingEnd() {
        val parkingPeriodMinute = userBookingViewModel.parkingPeriodMinute.value!!
        val durationHour = java.time.Duration.ofHours(parkingPeriodMinute.div(60).toLong())
        val durationMinute = java.time.Duration.ofMinutes(parkingPeriodMinute.rem(60).toLong())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val currentTime = LocalDateTime.now()
        val endTime = currentTime.plus(durationHour).plus(durationMinute)
        userBookingViewModel.setParkingEnd(endTime.format(formatter))
    }

    private fun updateParkingStartEndFirestore() {
        bookingRef = db.collection("users").document(userIdViewModel.userId).collection("bookings")
            .document(userBookingViewModel.bookingId.value.toString())
        val parkingStart = userBookingViewModel.parkingStart.value
        val parkingEnd = userBookingViewModel.parkingEnd.value
        val parkingStartMilis = System.currentTimeMillis()
        bookingRef.update("parkingStart", parkingStart, "parkingEnd", parkingEnd, "parkingStartMilis", parkingStartMilis)
            .addOnSuccessListener {
                //nothing here
        }
            .addOnFailureListener {
                Log.d("BookingConfirmedFragment", "failed with $it")
            }
    }

    private fun changeNavigationBarColour() {
        val activity = requireActivity()
        val window = activity.window
        val context = activity.applicationContext
        window.setNavigationBarColor(ContextCompat.getColor(context, R.color.background_color))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.needAssistanceButton.setOnClickListener { needAssistance() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun needAssistance() {
        val intent = Intent(requireContext(), NeedAssistanceActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}