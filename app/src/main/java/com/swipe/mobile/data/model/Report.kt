package com.swipe.mobile.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.swipe.mobile.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report (
    override val id: Int = 0,
    val title: String? = "",

    @SerializedName("description")
    val desc: String? = "",

    val category: String? = "",

    val location: String? = "",

    @SerializedName("target_donation")
    val targetDonation: Int? = 0,

    val status: String? = "initial",

    @SerializedName("due_date")
    val dueDate: String? = "",

    @SerializedName("pic_name")
    val picName: String? = "",

    @SerializedName("docs")
    val docLink: String? = "",

    @SerializedName("donate")
    val currentDonation: Int? = 0,

    @SerializedName("image")
    val images: List<ReportImage>? = null,
) : BaseModel(id), Parcelable