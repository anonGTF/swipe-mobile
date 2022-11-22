package com.swipe.mobile.ui.donate

import com.swipe.mobile.base.BaseViewModel
import com.swipe.mobile.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DonateViewModel @Inject constructor(
    private val api: ApiService
): BaseViewModel() {
    fun donate(id: Int, amount: Int) = callApiReturnLiveData(
        apiCall = { api.donate(id, amount) }
    )

    fun withdraw(id: Int) = callApiReturnLiveData(
        apiCall = { api.withdraw(id) }
    )
}