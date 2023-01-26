package com.abing.letak.advancebookingactivity.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.advancebookingactivity.adapter.AdvanceLotAdapter
import com.abing.letak.databinding.FragmentAdvanceLotSelectionBinding
import com.abing.letak.model.ParkingLot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdvanceLotSelectionFragment : Fragment() {

    private val parkingLots: MutableList<ParkingLot> = mutableListOf()
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentAdvanceLotSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdvanceLotSelectionBinding.inflate(layoutInflater)


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
        binding.availableParkingLotRv.layoutManager = LinearLayoutManager(requireContext())
        binding.availableParkingLotRv.adapter =
            AdvanceLotAdapter(requireContext(), parkingLots)
        binding.cancelButton.setOnClickListener { cancelOrderNow() }
    }


    private fun cancelOrderNow() {
        parkingLots.clear()
        activity?.finish()
    }
}