package com.abing.letak.mainmenufragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityViewCommand
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.abing.letak.data.vehicles
import com.abing.letak.databinding.FragmentProfileBinding
import com.abing.letak.registervehicle.RegisterVehicleActivity
import com.abing.letak.sampleadapters.VehicleAdapter

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val dataset = vehicles

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userVehicleRv.adapter = VehicleAdapter(dataset)
        binding.userVehicleRv.layoutManager = LinearLayoutManager(requireContext())
        binding.registerVehicleButton.setOnClickListener { registerVehicle() }
    }

    private fun registerVehicle() {
        val intent = Intent(requireContext(), RegisterVehicleActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}