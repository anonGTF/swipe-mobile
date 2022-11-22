package com.swipe.mobile.ui.main.myactivities

import com.swipe.mobile.base.BaseViewModel
import com.swipe.mobile.data.model.Notification
import com.swipe.mobile.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyActivitiesViewModel @Inject constructor(
    private val api: ApiService
): BaseViewModel() {

    fun getNotification() = getDummyNotify()

    fun getReports() = callApiReturnLiveData(
        apiCall = { api.getReports(1) }
    )

    private fun getDummyNotify() = listOf(
            Notification(
                1,
                "Your report has been taken"
            ),
            Notification(
                2,
                "Your report has been taken"
            ),
            Notification(
                3,
                "Your report has been taken"
            ),
            Notification(
                4,
                "Your report has been taken"
            )
        )
}