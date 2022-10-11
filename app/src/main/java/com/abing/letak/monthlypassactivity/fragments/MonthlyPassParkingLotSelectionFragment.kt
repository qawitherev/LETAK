package com.abing.letak.monthlypassactivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.R
import com.abing.letak.data.parkingLotList
import com.abing.letak.databinding.FragmentMonthlyPassParkingLotSelectionBinding
import com.abing.letak.monthlypassactivity.adapter.PassParkingLotAdapter

class MonthlyPassParkingLotSelectionFragment : Fragment() {
    private var _binding: FragmentMonthlyPassParkingLotSelectionBinding? = null
    private val binding get() = _binding!!
    private val dataset = parkingLotList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassParkingLotSelectionBinding
            .inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.availableParkingLotRv.layoutManager = LinearLayoutManager(requireContext())
        binding.availableParkingLotRv.adapter = PassParkingLotAdapter(requireContext(), dataset)
        binding.cancelButton.setOnClickListener { activity?.finish() }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}