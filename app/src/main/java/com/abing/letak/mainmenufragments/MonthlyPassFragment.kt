package com.abing.letak.mainmenufragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abing.letak.databinding.FragmentMonthlyPassBinding
import com.abing.letak.monthlypassactivity.MonthlyPassActivity

class MonthlyPassFragment : Fragment() {
    private var _binding: FragmentMonthlyPassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMonthlyPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.purchasePassButton.setOnClickListener { startMonthlyPass() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun startMonthlyPass() {
        val intent = Intent(requireContext(), MonthlyPassActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}