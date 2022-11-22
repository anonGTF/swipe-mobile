package com.swipe.mobile.data.model

data class BaseResponse<T> (
    val data: T? = null,
    val meta: Meta? = null,
    val message: String = ""
)