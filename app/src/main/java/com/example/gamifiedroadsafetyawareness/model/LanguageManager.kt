package com.example.gamifiedroadsafetyawareness.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Stores the user's chosen app language (English/Tagalog), mirroring the SharedPreferences-backed
 * singleton pattern already used by AuthManager/XpManager for cross-cutting app state.
 */
class LanguageManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANGUAGE = "language_code"
        const val ENGLISH = "en"
        const val TAGALOG = "tl"
    }

    fun getLanguage(): String = prefs.getString(KEY_LANGUAGE, ENGLISH) ?: ENGLISH

    fun setLanguage(code: String) {
        prefs.edit().putString(KEY_LANGUAGE, code).apply()
    }
}
