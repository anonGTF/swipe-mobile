package com.swipe.mobile.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper {
    fun saveInt(key: String, value: Int) {
        val editor = mSharedPreferences?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }

    fun getInt(key: String): Int? {
        return if (isKeyExists(key)) {
            mSharedPreferences?.getInt(key, 0)
        } else 0
    }

    fun saveBoolean(key: String, value: Boolean) {
        val editor = mSharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(key: String): Boolean? {
        return if (isKeyExists(key)) {
            mSharedPreferences?.getBoolean(key, false)
        } else {
            false
        }
    }

    fun getBooleanNull(key: String): Boolean? {
        return if (isKeyExists(key)) {
            mSharedPreferences?.getBoolean(key, false)
        } else {
            null
        }
    }

    fun saveFloat(key: String, value: Float) {
        val editor = mSharedPreferences?.edit()
        editor?.putFloat(key, value)
        editor?.apply()
    }

    fun getFloat(key: String): Float? {
        return if (isKeyExists(key)) {
            mSharedPreferences?.getFloat(key, 0.0f)
        } else 0.0f
    }


    fun saveLong(key: String, value: Long) {
        val editor = mSharedPreferences?.edit()
        editor?.putLong(key, value)
        editor?.apply()
    }

    fun getLong(key: String): Long? {
        return if (isKeyExists(key)) {
            mSharedPreferences?.getLong(key, 0)
        } else 0
    }

    fun saveString(key: String, value: String) {
        val editor = mSharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(key: String): String? {
        return if (isKeyExists(key)) {
            mSharedPreferences?.getString(key, null)
        } else null
    }

    fun clearSession() {
        val editor = mSharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }

    fun deleteValue(key: String): Boolean {
        if (this.isKeyExists(key)) {
            val editor = mSharedPreferences?.edit()
            editor?.remove(key)
            editor?.apply()
            return true
        }

        return false
    }

    private fun isKeyExists(key: String): Boolean {
        val map = mSharedPreferences?.all
        return map != null && map.containsKey(key)
    }

    companion object {

        private var instance: PreferenceHelper? = null
        private var mSharedPreferences: SharedPreferences? = null

        fun init(context: Context) {
            mSharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }

        fun instance(): PreferenceHelper? {
            if (instance == null) {
                validateInitialization()
                synchronized(PreferenceHelper::class.java) {
                    if (instance == null) {
                        instance = PreferenceHelper()
                    }
                }
            }
            return instance
        }

        private fun validateInitialization() {
            if (mSharedPreferences == null)
                throw Exception("SuitSave Library must be initialized inside your application class by calling SuitSave.init(getApplicationContext)")
        }
    }
}