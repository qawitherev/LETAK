package com.abing.letak.mainmenufragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.databinding.FragmentOrderNowBinding
import com.abing.letak.model.ParkingLot
import com.abing.letak.ordernowactivity.OrderNowActivity
import com.abing.letak.parkinglotadapter.ParkingLotAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class OrderNowFragment : Fragment() {
    private var _binding: FragmentOrderNowBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val parkingLots: MutableList<ParkingLot> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderNowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getParkingLot()
        binding.letakNowButton.setOnClickListener {
            startOrderNowActivity()
        }
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
        binding.aLittleInsightRecyclerView.adapter =
            ParkingLotAdapter(requireContext(), parkingLots)
        binding.aLittleInsightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun startOrderNowActivity() {
        val intent = Intent(activity, OrderNowActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parkingLots.clear()
        _binding = null
    }
}