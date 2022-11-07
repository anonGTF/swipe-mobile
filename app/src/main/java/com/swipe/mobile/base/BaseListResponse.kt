package com.swipe.mobile.base

import com.google.gson.annotations.SerializedName

data class BaseListResponse<T>(

    @SerializedName("results")
    val results: List<T>,

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null
)