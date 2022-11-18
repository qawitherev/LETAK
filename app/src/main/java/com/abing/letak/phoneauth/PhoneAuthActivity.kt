package com.abing.letak.phoneauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.abing.letak.R
import com.abing.letak.databinding.ActivityPhoneAuthBinding
import com.abing.letak.utils.lightStatusBar

class PhoneAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneAuthBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //utils
        lightStatusBar(window, true, true)

        //navigation init
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.phone_auth_container_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}