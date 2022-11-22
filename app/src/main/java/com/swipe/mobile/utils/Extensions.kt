package com.swipe.mobile.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.swipe.mobile.data.local.RealmHelper
import io.realm.RealmObject
import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*

const val BASE_IMAGE_URL = "https://eponline.com/-/media/ENV/eponline/Images/2019/11/3EnvironmentalIssues.jpg"

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun String.toDate(pattern: String = "yyyy-MM-dd"): Date? =
    try {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun Int?.orZero() = this ?: 0

fun Int?.orOne() = this ?: 1

fun Double?.orZero() = this ?: 0.0

fun Boolean?.orFalse() = this ?: false

fun String.withImageUrl() = BASE_IMAGE_URL + this

fun String.trim(maxLength: Int) =
    if (this.length > maxLength)
        "${this.take(maxLength - 3)}..."
    else
        this

fun View.visible() = run { this.visibility = View.VISIBLE }

fun View.gone() = run { this.visibility = View.GONE }

fun ViewGroup.inflate(@LayoutRes layoutResId: Int): View = LayoutInflater.from(context)
    .inflate(layoutResId, this, false)

fun RecyclerView.removeItemDecorations() {
    while (this.itemDecorationCount > 0) {
        this.removeItemDecorationAt(0)
    }
}
