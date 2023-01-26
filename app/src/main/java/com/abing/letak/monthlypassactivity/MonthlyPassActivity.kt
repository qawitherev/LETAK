package com.abing.letak.monthlypassactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupActionBarWithNavController
import com.abing.letak.R
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class MonthlyPassActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_pass)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.pass_container_fragment) as NavHost
        navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.pass_toolbar))
        setupActionBarWithNavController(navController)

        lightStatusBar(window, false, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}