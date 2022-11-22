package com.swipe.mobile.data.model

import com.google.gson.annotations.SerializedName

data class Meta (
    @SerializedName("current_page")
    var currentPage: Int = 0,

    @SerializedName("to")
    var lastPage: Int = 0
)