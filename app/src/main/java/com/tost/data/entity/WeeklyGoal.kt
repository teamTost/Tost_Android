package com.tost.data.entity

import com.google.gson.annotations.SerializedName
import com.tost.data.service.request.SaveWeeklyGoalParams

/**
 * Created By Malibin
 * on 12월 24, 2020
 */

data class WeeklyGoal(
    @SerializedName("partOne")
    val part1: Int,
    @SerializedName("partTwo")
    val part2: Int,
    @SerializedName("partThree")
    val part3: Int,
    @SerializedName("partFour")
    val part4: Int,
    @SerializedName("partFive")
    val part5: Int,
    @SerializedName("partSix")
    val part6: Int,
) {
    fun toParams() = SaveWeeklyGoalParams(
        partOne = part1.toString(),
        partTwo = part2.toString(),
        partThree = part3.toString(),
        partFour = part4.toString(),
        partFive = part5.toString(),
        partSix = part6.toString(),
    )
}