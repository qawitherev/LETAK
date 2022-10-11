package com.abing.letak.needassistance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.R
import com.abing.letak.databinding.ActivityExtendParkingBinding
import com.abing.letak.databinding.ActivityNeedAssistanceBinding
import com.abing.letak.utils.lightStatusBar

class NeedAssistanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNeedAssistanceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNeedAssistanceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lightStatusBar(window, false, true)
    }

    override fun onStart() {
        binding.cancelButton.setOnClickListener { cancelAssistance() }
        super.onStart()
    }

    private fun cancelAssistance() {
        finish()
    }
}