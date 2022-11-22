package com.swipe.mobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportImage(
    val image: String = ""
): Parcelable