package com.swipe.mobile.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class User: RealmObject() {
    @SerializedName("id")
    var id: Int? = 0

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("avatar")
    var avatar: String? = ""
}