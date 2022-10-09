package com.abing.letak.ordernowactivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.R
import com.abing.letak.data.parkingLotList
import com.abing.letak.databinding.FragmentParkingLotSelectionBinding
import com.abing.letak.ordernowactivity.adapter.OrderNowParkingLotAdapter

class ParkingLotSelectionFragment : Fragment() {
    private var _binding: FragmentParkingLotSelectionBinding? = null
    private val binding get() = _binding!!

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
        val data = parkingLotList
        binding.availableParkingLotRv.layoutManager =LinearLayoutManager(requireContext())
        binding.availableParkingLotRv.adapter = OrderNowParkingLotAdapter(requireContext(),data)
    }
}