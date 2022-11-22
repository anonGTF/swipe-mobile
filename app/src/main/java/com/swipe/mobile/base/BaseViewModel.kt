package com.swipe.mobile.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swipe.mobile.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class BaseViewModel: ViewModel() {

    fun <T> callApiReturnLiveData(
        apiCall: suspend () -> Response<T>,
        handleBeforePostSuccess: (Response<T>) -> Unit = {},
        handleAfterPostSuccess: (Response<T>) -> Unit = {}
    ): LiveData<Resource<T>> {
        val data: MutableLiveData<Resource<T>> = MutableLiveData()
        viewModelScope.launch {
            data.postValue(Resource.Loading())
            try {
                val response = apiCall()
                if (response.isSuccessful && response.body() != null) {
                    handleBeforePostSuccess(response)
                    data.postValue(Resource.Success(response.body()!!))
                } else {
                    throw Exception(response.message())
                }
            } catch (e: Exception) {
                data.postValue(Resource.Error(e.localizedMessage ?: "unknown error"))
            }
        }
        return data
    }

}