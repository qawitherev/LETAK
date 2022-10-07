package com.abing.letak.ordernowactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupActionBarWithNavController
import com.abing.letak.R
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class OrderNowActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_now)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.order_now_container_fragment) as NavHost
        navController = navHostFragment.navController

        lightStatusBar(window, false, false)

        setSupportActionBar(findViewById(R.id.order_now_toolbar))
        setupActionBarWithNavController(navController)
    }
}