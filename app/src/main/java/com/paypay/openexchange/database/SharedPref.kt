package com.paypay.openexchange.database

import android.content.Context
import android.content.SharedPreferences
import com.paypay.openexchange.util.Constants

object SharedPref {
    lateinit var sharedPrefs: SharedPreferences

    fun init(context: Context) {
        // saving data locally and privately
        sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREF_STORE_KEY, Context.MODE_PRIVATE)
    }

    fun save(key: String, dateInLong: Long) { // saving data locally and privately
        val editor = sharedPrefs.edit()
        try {
            editor.putLong(key, dateInLong)
            editor.apply()
        } catch (e: Exception) {
            //no-op
        }
    }

    fun save(key: String, value: String?) { // saving data locally and privately
        val editor = sharedPrefs.edit()
        try {
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            //no-op
        }
    }

    @JvmStatic
    fun save(key: String, value: Boolean) { // saving data locally and privately
        val editor = sharedPrefs.edit()
        try {
            editor.putBoolean(key, value)
            editor.apply()
        } catch (e: Exception) {
            //no-op
        }
    }

    fun save(key: String, value: Int) { // saving data locally and privately
        val editor = sharedPrefs.edit()
        try {
            editor.putInt(key, value)
            editor.apply()
        } catch (e: Exception) {
            //no-op
        }
    }

    fun save(key: String, value: Float) { // saving data locally and privately
        val editor = sharedPrefs.edit()
        try {
            editor.putFloat(key, value)
            editor.apply()
        } catch (e: Exception) {
            //no-op
        }
    }

    @JvmStatic
    operator fun get(key: String): String? {
        return sharedPrefs.getString(key, null)
    }

    fun getLongValue(key: String): Long {
        return sharedPrefs.getLong(key, 0)
    }

    fun getValue(key: String): Boolean {
        return sharedPrefs.getBoolean(key, false)
    }

    fun getIntValue(key: String): Int {
        return sharedPrefs.getInt(key, 0)
    }

    fun getFloatValue(key: String): Float {
        return sharedPrefs.getFloat(key, 0f)
    }

    fun remove(key: String) {
        val editor = sharedPrefs.edit()
        try {
            editor.remove(key)
            editor.apply()
        } catch (e: Exception) { //TODO log to sentry
            //no-op
        }
    }

    fun contains(key: String): Boolean {
        return sharedPrefs.contains(key)
    }
}