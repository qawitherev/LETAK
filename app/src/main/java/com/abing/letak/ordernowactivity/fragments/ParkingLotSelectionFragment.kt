package com.abing.letak.ordernowactivity.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.databinding.FragmentParkingLotSelectionBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.model.UserBooking
import com.abing.letak.ordernowactivity.adapter.OrderNowParkingLotAdapter
import com.abing.letak.viewmodel.UserBookingViewModel
import com.abing.letak.viewmodel.UserIdViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ParkingLotSelectionFragment : Fragment() {
    private var _binding: FragmentParkingLotSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val parkingLots: MutableList<ParkingLot> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParkingLotSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init
        db = FirebaseFirestore.getInstance()

        getParkingLot()

    }

    private fun getParkingLot() {
        val lotRef = db.collection("parkingLots")
        lotRef.get()
            .addOnSuccessListener {
                for (document in it) {
                    parkingLots.add(document.toObject())
                    Log.d("order parking lot list", "${parkingLots.size}")
                }
                initRecyclerView()
            }
            .addOnFailureListener {
                Log.w("getParkingLot", "getParkingLot failed with", it)
            }
    }

    private fun initRecyclerView() {
        binding.availableParkingLotRv.layoutManager =LinearLayoutManager(requireContext())
        binding.availableParkingLotRv.adapter = OrderNowParkingLotAdapter(requireContext(),parkingLots)
        binding.cancelButton.setOnClickListener { cancelOrderNow() }
    }


    private fun cancelOrderNow() {
        parkingLots.clear()
        activity?.finish()
    }
}