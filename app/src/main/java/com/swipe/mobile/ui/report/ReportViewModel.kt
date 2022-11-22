package com.swipe.mobile.ui.report

import com.swipe.mobile.base.BaseViewModel
import com.swipe.mobile.data.local.Preferences
import com.swipe.mobile.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val api: ApiService,
    private val pref: Preferences
): BaseViewModel() {

    fun takeReport(id: Int, picName: String, targetDonation: Int) = callApiReturnLiveData(
        apiCall = { api.takeReport(id, picName, targetDonation) }
    )

    fun createReport(title: String, desc: String, category: String, location: String) = callApiReturnLiveData(
        apiCall = { api.createReport(title, desc, category, location) }
    )

    fun finishReport(id: Int) {}

    fun getCategories() = listOf(
        "Environment",
        "Social",
        "Education",
        "Health"
    )

    fun isCommunity() = pref.isCommunity
}