package com.abing.letak.ordernowactivity.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.abing.letak.R
import com.abing.letak.databinding.FragmentBookingConfirmedBinding
import com.abing.letak.needassistance.NeedAssistanceActivity
import com.abing.letak.viewmodel.UserBookingViewModel

class BookingConfirmedFragment : Fragment() {
    private var _binding: FragmentBookingConfirmedBinding? = null
    private val binding get() = _binding!!
    private val userBookingViewModel: UserBookingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingConfirmedBinding.inflate(layoutInflater)

        changeNavigationBarColour()
        return binding.root
    }

    private fun changeNavigationBarColour() {
        val activity = requireActivity()
        val window = activity.window
        val context = activity.applicationContext
        window.setNavigationBarColor(ContextCompat.getColor(context, R.color.background_color))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.needAssistanceButton.setOnClickListener { needAssistance() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun needAssistance() {
        val intent = Intent(requireContext(), NeedAssistanceActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}