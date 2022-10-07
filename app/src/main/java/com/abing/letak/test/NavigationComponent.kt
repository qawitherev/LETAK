package com.abing.letak.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.abing.letak.R

class NavigationComponent : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_component)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.test_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setSupportActionBar(findViewById(R.id.toolbar_test))
        setupActionBarWithNavController(navController)
    }
}