package com.compultra.hcare.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.compultra.hcare.R
import com.compultra.hcare.network.AuthApi
import com.compultra.hcare.ui.auth.AuthActivity
import com.compultra.hcare.ui.doctor.BrowseDoctorActivity
import com.compultra.hcare.ui.patient.BrowsePatientActivity
import com.compultra.hcare.util.AUTH_TOKEN
import com.compultra.hcare.util.MY_SHARED_PREFS
import com.compultra.hcare.util.USER_TYPE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(AUTH_TOKEN, null)
//        Log.d("token", token?:"null")
        val userType = getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(USER_TYPE, null)
        if (token != null && userType != null) {
            GlobalScope.launch {
                if(AuthApi.retrofitService.checkAuth(token).message == "successful"){
                    when (userType) {
                        "doctor" -> BrowseDoctorActivity.startAlone(this@SplashActivity)
                        "patient" -> BrowsePatientActivity.startAlone(this@SplashActivity)
                        else -> AuthActivity.startAlone(this@SplashActivity)
                    }
                } else {
                    AuthActivity.startAlone(this@SplashActivity)
                }
            }
        } else {
            AuthActivity.startAlone(this)
        }
    }
}