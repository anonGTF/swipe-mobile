package com.swipe.mobile.utils

fun safeDivision(numerator: Int?, denominator: Int?, defaultAnswer: Int = 0): Int =
    if (numerator == null || denominator == null || denominator == 0) {
        defaultAnswer
    } else {
        numerator / denominator
    }
