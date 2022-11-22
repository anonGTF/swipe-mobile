package com.swipe.mobile.ui.auth

import com.swipe.mobile.base.BaseViewModel
import com.swipe.mobile.data.local.Preferences
import com.swipe.mobile.data.remote.ApiService
import com.swipe.mobile.utils.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val api: ApiService,
    private val pref: Preferences
): BaseViewModel() {

    fun login(email: String, password: String) = callApiReturnLiveData(
        apiCall = { api.login(email, password) },
        handleBeforePostSuccess = { response ->
            pref.saveToken(response.body()?.token)
            pref.saveUserId(response.body()?.id.orZero())
            pref.saveIsCommunity(false)
        }
    )

    fun loginCommunity(email: String, password: String) = callApiReturnLiveData(
        apiCall = { api.loginCommunity(email, password) },
        handleBeforePostSuccess = { response ->
            pref.saveToken(response.body()?.token)
            pref.saveUserId(response.body()?.id.orZero())
            pref.saveIsCommunity(true)
        }
    )

    fun register(name: String, email: String, password: String) = callApiReturnLiveData(
        apiCall = { api.register(name, email, password) }
    )
}