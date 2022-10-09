package com.abing.letak.ordernowactivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.abing.letak.databinding.FragmentSpaceSelectionBinding

class SpaceSelectionFragment : Fragment() {
    private var _binding: FragmentSpaceSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpaceSelectionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.spaceSelectionContinueButton.setOnClickListener { continueToParkingConfirmation(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun continueToParkingConfirmation(view: View) {
        val action = SpaceSelectionFragmentDirections
            .actionSpaceSelectionFragmentToParkingConfirmationFragment()
        view.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}