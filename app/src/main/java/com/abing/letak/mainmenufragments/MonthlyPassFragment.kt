package com.abing.letak.mainmenufragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.abing.letak.R
import com.abing.letak.databinding.FragmentMonthlyPassBinding
import com.abing.letak.model.ParkingSpace
import com.abing.letak.monthlypassactivity.MonthlyPassActivity
import com.abing.letak.monthlypassactivity.viewmodel.PassBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class MonthlyPassFragment : Fragment() {

    private val TAG = "MonthlyPassFragment"
    private val passBookingViewModel: PassBookingViewModel by activityViewModels()
    private val userIdViewModel: UserIdViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentMonthlyPassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassBinding.inflate(inflater, container, false)
        initSetup()
        checkStatus()
        return binding.root
    }

    private fun initSetup() {
        db = FirebaseFirestore.getInstance()
    }

    private fun checkStatus() {
        val userRef = db.collection("users").document(userIdViewModel.userId)
        userRef.get().addOnSuccessListener {
            val status = it.getBoolean("monthlyPassStatus")!!
            passBookingViewModel.setPassId(it.getString("activePassId").toString())
            if (!status){
                binding.passInactiveGroup.visibility = View.VISIBLE
                binding.activeGroup.visibility = View.GONE
            }else {
                binding.apply {
                    passInactiveGroup.visibility = View.GONE
                    activeGroup.visibility = View.VISIBLE
                }
                activePass()
            }
        }
    }

    private fun activePass() {
        setupDetails()
        binding.parkButton.setOnClickListener { passPark() }
        binding.unparkButton.setOnClickListener { passUnpark() }
    }

    private fun passUnpark() {
        //start unpark
        val lotRef = db.collection("parkingLots").document(passBookingViewModel.lotId.value.toString())
        lotRef.update("lotOccupied", FieldValue.increment(-1))
        val spaceRef = lotRef.collection("parkingSpaces").document(passBookingViewModel.spaceId.value.toString())
        spaceRef.update("spaceEmpty", true)
        val bookingRef = db.collection("users").document(userIdViewModel.userId).collection("pass")
            .document(passBookingViewModel.passId.value.toString())
        bookingRef.update("parked", false)
        binding.apply {
            parkButton.visibility = View.VISIBLE
            unparkButton.visibility = View.GONE
        }
    }

    private fun passPark() {
        spaceManagement()
        binding.unparkButton.setOnClickListener { passUnpark() }
    }

    private fun spaceManagement() {
        val spaces = arrayListOf<ParkingSpace>()
        val lotRef = db.collection("parkingLots").document(passBookingViewModel.lotId.value.toString()).collection("parkingSpaces")
        lotRef.whereEqualTo("spaceType", "Green").whereEqualTo("spaceEmpty", true).get().addOnSuccessListener {
            if (it.isEmpty){
                Toast.makeText(requireContext(), R.string.no_available_space, Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }else {
                for (space in it){
                    spaces.add(space.toObject())
                }
                if (!notTime()){
                    Toast.makeText(requireContext(), R.string.check_time, Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }else {
                    val selectedSpaceId = spaces[0].spaceId.toString()
                    passBookingViewModel.setSpaceId(selectedSpaceId)
                    val bookingRef = db.collection("users").document(userIdViewModel.userId).collection("pass")
                        .document(passBookingViewModel.passId.value.toString())
                    bookingRef.update("spaceId", passBookingViewModel.spaceId.value)
                    val spaceRef = db.collection("parkingLots").document(passBookingViewModel.lotId.value.toString())
                        .collection("parkingSpaces").document(selectedSpaceId)
                    spaceRef.update("spaceEmpty", false)
                    val occupiedRef = db.collection("parkingLots").document(passBookingViewModel.lotId.value.toString())
                    occupiedRef.update("lotOccupied", FieldValue.increment(1))
                    bookingRef.update("parked", true)
                    binding.apply {
                        parkButton.visibility = View.GONE
                        unparkButton.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    private fun notTime(): Boolean{
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return currentHour in 8..18
    }

    private fun setupDetails() {
        val passRef = db.collection("users").document(userIdViewModel.userId)
            .collection("pass").document(passBookingViewModel.passId.value.toString())
        passRef.get().addOnSuccessListener {
            binding.parkingLotName.text = it.getString("lotName")
            binding.daysLeftTextCardText.text = calculateDaysLeft(it).toString()
            passBookingViewModel.setIsParked(it.getBoolean("parked")!!)
            val isParked = passBookingViewModel.isParked.value!!
            if (!isParked){
                binding.apply {
                    parkButton.visibility = View.VISIBLE
                    unparkButton.visibility = View.GONE
                }
            }else {
                binding.apply {
                    parkButton.visibility = View.GONE
                    unparkButton.visibility = View.VISIBLE
                }
            }

            //setting the lotid
            passBookingViewModel.setLotId(it?.getString("lotId").toString())
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun calculateDaysLeft(it: DocumentSnapshot?): Long {
        passBookingViewModel.setStartDate(it?.getString("startDate").toString())
        passBookingViewModel.setEndDate(it?.getString("endDate").toString())
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val startDate =
            dateFormat.parse(passBookingViewModel.startDate.value.toString())?.toInstant()
        val endDate = dateFormat.parse(passBookingViewModel.endDate.value.toString())?.toInstant()
        val dateToday = Instant.now()
        val daysBetween = Duration.between(dateToday, endDate)
        return daysBetween.toDays()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.purchasePassButton.setOnClickListener { startMonthlyPass() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startMonthlyPass() {
        val intent = Intent(requireContext(), MonthlyPassActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}