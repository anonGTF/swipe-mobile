package com.swipe.mobile.data.model

import com.swipe.mobile.base.BaseModel

data class Notification(
    override val id: Int,
    val message: String
): BaseModel(id)
