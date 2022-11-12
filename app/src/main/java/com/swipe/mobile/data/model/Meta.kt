package com.swipe.mobile.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class Meta : RealmObject() {
    @SerializedName("current_page")
    var currentPage: Int = 0

    @SerializedName("to")
    var lastPage: Int = 0
}