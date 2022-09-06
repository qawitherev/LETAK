package com.abing.letak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.abing.letak.databinding.ActivityMainMenuBinding
import com.abing.letak.mainmenufragments.MonthlyPassFragment
import com.abing.letak.mainmenufragments.OrderNowFragment
import com.abing.letak.utils.lightStatusBar
import com.abing.letak.utils.setFullScreen

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, OrderNowFragment()).commit()
        lightStatusBar(window, false, false)
        setSupportActionBar(binding.mainMenuToolbar)

        navBotNavigations()
    }

    private fun navBotNavigations() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.order_now -> {
                    setCurrentFragment(OrderNowFragment())
                    true
                }
                R.id.monthly_pass -> {
                    setCurrentFragment(MonthlyPassFragment())
                    true
                }
                R.id.notifications -> {
                    // TODO: notifications activity
                    true
                }
                else -> {
                    // TODO: profile activity activity
                    true
                }

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setCurrentFragment(currentFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, currentFragment)
            commit()
        }
    }
}