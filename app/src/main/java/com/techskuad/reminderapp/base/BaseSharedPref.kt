package com.techskuad.reminderapp.base

import android.content.SharedPreferences

abstract class BaseSharedPref constructor(
    private val preferences: SharedPreferences
) {
    private val editor: SharedPreferences.Editor = preferences.edit()

    /*--Edit--*/
    fun save(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun save(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }
    /*----*/


    /*--Get--*/
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getString(key: String, defValue: String?): String? {
        return preferences.getString(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }
    /*----*/
}