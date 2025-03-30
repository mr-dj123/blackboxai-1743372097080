package com.phinma.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val sharedPref: SharedPreferences = 
        context.getSharedPreferences("PHINMA_PREFS", Context.MODE_PRIVATE)

    fun saveSchoolId(schoolId: Int) {
        sharedPref.edit().putInt("SELECTED_SCHOOL_ID", schoolId).apply()
    }

    fun getSchoolId(): Int {
        return sharedPref.getInt("SELECTED_SCHOOL_ID", -1)
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPref.edit().putBoolean("IS_LOGGED_IN", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean("IS_LOGGED_IN", false)
    }

    fun saveUserEmail(email: String) {
        sharedPref.edit().putString("USER_EMAIL", email).apply()
    }

    fun getUserEmail(): String {
        return sharedPref.getString("USER_EMAIL", "") ?: ""
    }

    fun clearPreferences() {
        sharedPref.edit().clear().apply()
    }
}