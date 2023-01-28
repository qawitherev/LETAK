package com.abing.letak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.abing.letak.managevehicle.ManageVehicleActivity
import com.abing.letak.monthlypassactivity.MonthlyPassActivity
import com.abing.letak.needassistance.NeedAssistanceActivity
import com.abing.letak.ordernowactivity.OrderNowActivity
import com.abing.letak.phoneauth.PhoneAuthActivity
import com.abing.letak.profilesetupactivity.ProfileSetupActivity
import com.abing.letak.registervehicle.RegisterVehicleActivity
import com.abing.letak.showprofileactivity.ShowProfileActivity
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //this is actually a splash screen
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}