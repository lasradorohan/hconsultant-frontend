package com.compultra.hcare.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.compultra.hcare.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val navView: BottomNavigationView = findViewById(R.id.doctor_nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.loginFragment,
                        R.id.registerFragment,
                )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    companion object {
        @JvmStatic
        fun startAlone(context: Context) {
            val starter = Intent(context, AuthActivity::class.java)
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(starter)
        }
    }
}