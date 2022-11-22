package com.swipe.mobile.ui.main.profile

import com.swipe.mobile.base.BaseViewModel
import com.swipe.mobile.data.local.Preferences
import com.swipe.mobile.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: ApiService,
    private val pref: Preferences
): BaseViewModel() {

    fun getProfile() = callApiReturnLiveData(
        apiCall = {
            if (pref.isCommunity) {
                api.getCommunityProfile()
            } else {
                api.getProfile()
            }
        }
    )

    fun logout() = callApiReturnLiveData(
        apiCall = { api.logout() },
        handleBeforePostSuccess = { resetPref() }
    )

    private fun resetPref() {
        pref.saveToken("")
        pref.saveUserId(0)
        pref.saveIsCommunity(false)
    }
}