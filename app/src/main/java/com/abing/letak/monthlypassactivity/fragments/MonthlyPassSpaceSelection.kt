package com.abing.letak.monthlypassactivity.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.abing.letak.R
import com.abing.letak.databinding.FragmentMonthlyPassParkingLotSelectionBinding
import com.abing.letak.databinding.FragmentMonthlyPassSpaceSelectionBinding

class MonthlyPassSpaceSelection : Fragment() {
    private var _binding: FragmentMonthlyPassSpaceSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassSpaceSelectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spaceSelectionContinueButton.setOnClickListener { continueToPassConfirmation(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun continueToPassConfirmation(view: View?) {
        val action = MonthlyPassSpaceSelectionDirections
            .actionMonthlyPassSpaceSelectionToMonthlyPassConfirmationFragment()
        view?.findNavController()?.navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}