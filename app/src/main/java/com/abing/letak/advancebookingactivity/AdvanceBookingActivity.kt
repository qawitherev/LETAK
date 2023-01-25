package com.abing.letak.advancebookingactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.R
import com.abing.letak.databinding.ActivityAdvanceBookingBinding
import com.abing.letak.utils.lightStatusBar

class AdvanceBookingActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAdvanceBookingBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lightStatusBar(window, false, true)
        //inflate layout
        _binding = ActivityAdvanceBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}