package com.swipe.mobile.ui.main.home

import com.swipe.mobile.base.BaseViewModel
import com.swipe.mobile.data.local.Preferences
import com.swipe.mobile.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: ApiService,
    private val pref: Preferences
): BaseViewModel() {

    fun getReports(page: Int) = callApiReturnLiveData(
        apiCall = { api.getReports(page) }
    )

    fun isCommunity() = pref.isCommunity
}