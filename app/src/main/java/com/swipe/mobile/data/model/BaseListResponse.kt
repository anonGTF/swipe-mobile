package com.swipe.mobile.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

open class BaseListResponse<T : RealmObject> : RealmObject() {

    @SerializedName("data")
    var data: RealmList<T>? = null

    @SerializedName("meta")
    var meta: Meta? = null
}