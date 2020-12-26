package com.tost.data.entity

import java.util.*

/**
 * Created By Malibin
 * on 12월 26, 2020
 */

data class EntireGoal(
    val level: Int,
    val startDate: Date,
    val endDate: Date,
) {
    fun getNowGoalProgress(now: Date): Int {
        val progressingTime = now.time - startDate.time
        val goalTime = endDate.time - startDate.time
        return (progressingTime.toDouble() / goalTime * 100).toInt()
    }
}
