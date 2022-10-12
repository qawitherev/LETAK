package com.abing.letak.registervehicle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abing.letak.R
import com.abing.letak.databinding.ActivityRegisterVehicleBinding
import com.abing.letak.utils.lightStatusBar

class RegisterVehicleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterVehicleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, false, true)

        setSupportActionBar(findViewById(R.id.register_vehicle_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.register_vehicle)
    }

}