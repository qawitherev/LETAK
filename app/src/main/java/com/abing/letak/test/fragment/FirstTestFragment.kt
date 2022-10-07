package com.abing.letak.test.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.abing.letak.R
import com.abing.letak.databinding.FragmentFirstTestBinding

class FirstTestFragment : Fragment() {
    private var _binding: FragmentFirstTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.moveButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_firstTestFragment_to_secondTestFragment)
        }
    }
}