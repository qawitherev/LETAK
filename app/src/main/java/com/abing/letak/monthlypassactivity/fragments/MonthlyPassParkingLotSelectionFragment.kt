package com.abing.letak.monthlypassactivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.advancebookingactivity.adapter.AdvanceLotAdapter
import com.abing.letak.databinding.FragmentMonthlyPassParkingLotSelectionBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.monthlypassactivity.adapter.PassLotAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class MonthlyPassParkingLotSelectionFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private val parkingLotsList = arrayListOf<ParkingLot>()
    private var _binding: FragmentMonthlyPassParkingLotSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassParkingLotSelectionBinding.inflate(layoutInflater)
        initSetup()

        getLot()
        return binding.root
    }

    private fun initSetup() {
        db = FirebaseFirestore.getInstance()
    }

    private fun getLot() {
        db.collection("parkingLots").get().addOnSuccessListener {
            for (lot in it){
                parkingLotsList.add(lot.toObject())
            }
            initRecyclerView()
        }
    }

    private fun initRecyclerView() {
        binding.availableParkingLotRv.layoutManager = LinearLayoutManager(requireContext())
        binding.availableParkingLotRv.adapter =
            PassLotAdapter(requireContext(), parkingLotsList)
        binding.cancelButton.setOnClickListener { cancelOrderNow() }
    }

    private fun cancelOrderNow() {
        parkingLotsList.clear()
        activity?.finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.availableParkingLotRv.layoutManager = LinearLayoutManager(requireContext())
        binding.cancelButton.setOnClickListener { activity?.finish() }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}