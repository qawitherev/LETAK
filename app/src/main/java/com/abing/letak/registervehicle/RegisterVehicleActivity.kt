package com.abing.letak.registervehicle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.R
import com.abing.letak.databinding.ActivityRegisterVehicleBinding

class RegisterVehicleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterVehicleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        activity finished
        binding.cancelButton.setOnClickListener { finish() }
    }

}