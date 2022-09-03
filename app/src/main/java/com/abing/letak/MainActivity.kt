package com.abing.letak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeActivity()
    }

    private fun welcomeActivity() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}