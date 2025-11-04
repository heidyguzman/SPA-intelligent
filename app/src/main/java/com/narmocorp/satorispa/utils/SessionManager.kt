package com.narmocorp.satorispa.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("SatoriSpaPrefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_SESSION = "session_active"
    }

    fun saveSessionPreference(isActive: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_SESSION, isActive)
        editor.apply()
    }

    fun getSessionPreference(): Boolean {
        return prefs.getBoolean(KEY_SESSION, false)
    }
}
