package com.abing.letak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abing.letak.databinding.ActivitySignUpBinding
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreen(window)
        lightStatusBar(window, true, true)

    }
}