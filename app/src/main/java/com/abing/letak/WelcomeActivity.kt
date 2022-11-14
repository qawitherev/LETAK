package com.abing.letak

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat
import com.abing.letak.databinding.ActivityWelcomeBinding
import com.abing.letak.utils.lightStatusBar
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        lightStatusBar(window, true, true)

        //init auth
        auth = FirebaseAuth.getInstance()

        //set onclick listener on buttons
        binding.loginButton.setOnClickListener { gotoLogin() }
        binding.registerButton.setOnClickListener { gotoRegister() }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun gotoRegister() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun gotoLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}