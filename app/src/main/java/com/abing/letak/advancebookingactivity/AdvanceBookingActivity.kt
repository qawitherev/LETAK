package com.abing.letak.advancebookingactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.R
import com.abing.letak.databinding.ActivityAdvanceBookingBinding

class AdvanceBookingActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAdvanceBookingBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflate layout
        _binding = ActivityAdvanceBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}