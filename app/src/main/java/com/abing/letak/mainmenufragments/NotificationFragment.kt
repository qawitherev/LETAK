package com.abing.letak.mainmenufragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.R
import com.abing.letak.data.notifications
import com.abing.letak.databinding.FragmentNotificationBinding
import com.abing.letak.databinding.FragmentOrderNowBinding
import com.abing.letak.model.UserBooking
import com.abing.letak.sampleadapters.NotificationAdapter
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class NotificationFragment : Fragment() {

    private val TAG = "NotificationFragment"
    private val bookingsArray = arrayListOf<UserBooking>()
    private lateinit var userRef: DocumentReference
    private val userIdViewModel: UserIdViewModel by activityViewModels()
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val dataset = notifications

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        initialSetup()
        getBookings()
        return binding.root
    }

    private fun initialSetup() {
        db = FirebaseFirestore.getInstance()
        userRef = db.collection("users").document(userIdViewModel.userId)
    }

    private fun getBookings() {
        userRef.collection("bookings").get()
            .addOnSuccessListener {
                for (booking in it){
                    bookingsArray.add(booking.toObject())
                }
                Log.d(TAG, "getBookings: ${bookingsArray.size}")
                binding.notificationsRv.adapter = NotificationAdapter(requireContext(), bookingsArray)
                binding.notificationsRv.layoutManager = LinearLayoutManager(requireContext())
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}