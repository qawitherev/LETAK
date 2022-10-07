package com.abing.letak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abing.letak.ordernowactivity.OrderNowActivity

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