package com.davinci.notasapp.data.local

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("notasapp_session", Context.MODE_PRIVATE)

    fun saveSession(token: String, userName: String) {
        prefs.edit().putString(KEY_TOKEN, token).putString(KEY_NAME, userName).apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun getUserName(): String = prefs.getString(KEY_NAME, null) ?: "Usuario"
    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()
    fun clear() = prefs.edit().clear().apply()

    private companion object {
        const val KEY_TOKEN = "token"
        const val KEY_NAME = "user_name"
    }
}