package com.abing.letak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.databinding.ActivityLoginBinding
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreen(window)
        lightStatusBar(window, true, true)
    }
}