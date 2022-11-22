package com.swipe.mobile.base

import com.google.gson.annotations.SerializedName

abstract class BaseModel(
    @SerializedName("_id")
    open val id: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseModel
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}