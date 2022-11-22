package com.swipe.mobile.data.model

import com.google.gson.annotations.SerializedName

data class User (
    val id: Int? = 0,

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("avatar")
    val avatar: String? = ""
)