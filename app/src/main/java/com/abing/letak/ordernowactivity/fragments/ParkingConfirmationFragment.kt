package com.abing.letak.ordernowactivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.abing.letak.R
import com.abing.letak.databinding.FragmentParkingConfirmationBinding

class ParkingConfirmationFragment : Fragment() {
    private var _binding: FragmentParkingConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParkingConfirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.letakButton.setOnClickListener { continueToParkingConfirmed(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun continueToParkingConfirmed(view: View) {
        val action = ParkingConfirmationFragmentDirections
            .actionParkingConfirmationFragmentToBookingConfirmedFragment()
        view.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}