package com.swipe.mobile.data.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.swipe.mobile.SwipeApplication
import com.swipe.mobile.data.local.Preferences.Companion.instance
import java.util.*

class Preferences private constructor() {
    private val mPrefs: SharedPreferences
    private val mEdit: SharedPreferences.Editor

    val token: String?
        get() = instance.mPrefs.getString(PREF_TOKEN, "")

    val userID: Int
        get() = instance.mPrefs.getInt(PREF_USER_ID, 0)

    val isCommunity: Boolean
        get() = instance.mPrefs.getBoolean(PREF_IS_COMMUNITY, false)

    fun saveToken(value: String?) {
        mEdit.putString(PREF_TOKEN, value)
        mEdit.apply()
    }

    fun saveUserId(value: Int) {
        mEdit.putInt(PREF_USER_ID, value)
        mEdit.apply()
    }

    fun saveIsCommunity(value: Boolean) {
        mEdit.putBoolean(PREF_IS_COMMUNITY, value)
        mEdit.apply()
    }

    companion object {
        const val PREF_TOKEN = "pref_token"
        const val PREF_USER_ID = "pref_id"
        const val PREF_IS_COMMUNITY = "pref_community"
        const val PREF_KEY = "pref_key"

        var INSTANCE: Preferences? = null
        val instance: Preferences
            get() {
                if (INSTANCE == null) INSTANCE = Preferences()
                return INSTANCE as Preferences
            }
    }

    init {
        val app: Application = SwipeApplication.instance
        mPrefs = app.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        mEdit = mPrefs.edit()
    }
}