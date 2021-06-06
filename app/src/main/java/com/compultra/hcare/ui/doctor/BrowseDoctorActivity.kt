package com.compultra.hcare.ui.doctor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.compultra.hcare.R
import com.compultra.hcare.network.DoctorDataApi
import com.compultra.hcare.ui.auth.AuthActivity
import com.compultra.hcare.util.AUTH_TOKEN
import com.compultra.hcare.util.MY_SHARED_PREFS
import com.compultra.hcare.util.USER_TYPE
import com.google.android.material.bottomnavigation.BottomNavigationView

class BrowseDoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_doctor)

        val navView: BottomNavigationView = findViewById(R.id.doctor_nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.doctor_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.consultationDoctorFragment,
                R.id.prescriptionDoctorFragment,
                R.id.chatListFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val token = getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE)
            .getString(AUTH_TOKEN, null)
        DoctorDataApi.setToken(token)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                DoctorDataApi.setToken(null)
                getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE).edit {
                    remove(AUTH_TOKEN)
                    remove(USER_TYPE)
                    commit()
                }
                AuthActivity.startAlone(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun startAlone(context: Context) {
            val starter = Intent(context, BrowseDoctorActivity::class.java)
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(starter)
        }
    }
}