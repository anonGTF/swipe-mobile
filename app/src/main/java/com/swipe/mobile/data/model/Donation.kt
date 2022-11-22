package com.swipe.mobile.data.model

data class Donation(
    val id: Int,
    val user: User? = null,
    val report: Report? = null,
    val amount: Int = 0,
)
