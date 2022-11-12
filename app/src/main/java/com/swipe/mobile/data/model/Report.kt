package com.swipe.mobile.data.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Report : RealmObject() {

    @PrimaryKey
    @SerializedName("id")
    var id: Int? = 0

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("description")
    var desc: String? = ""

    @SerializedName("category")
    var category: String? = ""

    @SerializedName("location")
    var location: String? = ""

    @SerializedName("target_donation")
    var targetDonation: Int? = 0

    @SerializedName("status")
    var status: String? = "initial"

    @SerializedName("due_date")
    var dueDate: String? = ""

    @SerializedName("pic_name")
    var picName: String? = ""

    @SerializedName("docs")
    var docLink: String? = ""

    @SerializedName("donate")
    var currentDonation: Int? = 0

    @SerializedName("image")
    var images: RealmList<String>? = null
}