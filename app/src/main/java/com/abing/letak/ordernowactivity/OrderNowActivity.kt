package com.abing.letak.ordernowactivity

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.abing.letak.R
import com.abing.letak.utils.lightStatusBar

class OrderNowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_now)

        lightStatusBar(window, false, false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.order_now_container_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        setSupportActionBar(findViewById(R.id.order_now_toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}