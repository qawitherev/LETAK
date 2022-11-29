package com.abing.letak.showprofileactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.R
import com.abing.letak.databinding.ActivityShowProfileBinding
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class ShowProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, true, true)
        setSupportActionBar(binding.showProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}