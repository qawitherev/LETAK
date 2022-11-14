package com.abing.letak

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abing.letak.databinding.ActivityMainMenuBinding
import com.abing.letak.mainmenufragments.MonthlyPassFragment
import com.abing.letak.mainmenufragments.NotificationFragment
import com.abing.letak.mainmenufragments.OrderNowFragment
import com.abing.letak.mainmenufragments.ProfileFragment
import com.abing.letak.utils.lightStatusBar
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, OrderNowFragment()).commit()
        setSupportActionBar(binding.orderMonthlyToolbar)
        binding.orderMonthlyToolbar.visibility = VISIBLE
        binding.notificationToolbar.visibility = GONE
        binding.profileToolbar.visibility = GONE
        lightStatusBar(window, false, false)

        navBotNavigations()

        //init values
        auth = FirebaseAuth.getInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.setting -> {
                auth.signOut()
                finish()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                true
            }else -> { super.onOptionsItemSelected(item)}
        }
    }

    private fun navBotNavigations() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.order_now -> {
                    setCurrentFragment(OrderNowFragment())
                    setSupportActionBar(binding.orderMonthlyToolbar)
                    binding.orderMonthlyToolbar.visibility = VISIBLE
                    binding.notificationToolbar.visibility = GONE
                    binding.profileToolbar.visibility = GONE
                    true
                }
                R.id.monthly_pass -> {
                    setCurrentFragment(MonthlyPassFragment())
                    setSupportActionBar(binding.orderMonthlyToolbar)
                    binding.orderMonthlyToolbar.visibility = VISIBLE
                    binding.notificationToolbar.visibility = GONE
                    binding.profileToolbar.visibility = GONE
                    true
                }
                R.id.notifications -> {
                    setCurrentFragment(NotificationFragment())
                    setSupportActionBar(binding.notificationToolbar)
                    binding.orderMonthlyToolbar.visibility = GONE
                    binding.notificationToolbar.visibility = VISIBLE
                    binding.profileToolbar.visibility = GONE
                    true
                }
                else -> {
                    setCurrentFragment(ProfileFragment())
                    setSupportActionBar(binding.profileToolbar)
                    binding.orderMonthlyToolbar.visibility = GONE
                    binding.notificationToolbar.visibility = GONE
                    binding.profileToolbar.visibility = VISIBLE
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