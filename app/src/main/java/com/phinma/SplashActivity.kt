package com.phinma

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.phinma.auth.LoginActivity
import com.phinma.utils.PreferenceHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthState()
        }, 1000)
    }

    private fun checkAuthState() {
        val prefs = PreferenceHelper(this)
        
        when {
            prefs.isLoggedIn() -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            prefs.getSchoolId() != -1 -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else -> {
                startActivity(Intent(this, SchoolSelectionActivity::class.java))
            }
        }
        finish()
    }
}