package com.abing.letak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.databinding.ActivityMainMenuBinding
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lightStatusBar(window, true, false)
    }
}