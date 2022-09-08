package com.abing.letak.mainmenufragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abing.letak.R
import com.abing.letak.databinding.FragmentMonthlyPassBinding
import com.abing.letak.databinding.FragmentOrderNowBinding

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}