package com.abing.letak.extendparking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.abing.letak.R
import com.abing.letak.databinding.ActivityExtendParkingBinding
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class ExtendParkingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtendParkingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtendParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, false, true)
    }
}