package com.tost.data.service.response

import com.tost.data.entity.EntireGoal
import com.tost.data.entity.WeeklyGoal
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By Malibin
 * on 12월 26, 2020
 */

class GoalsResponse(
    val userIdx: String,
    val targetDate: String,
    val targetLevel: Int,
    val partOne: String,
    val partTwo: String,
    val partThree: String,
    val partFour: String,
    val partFive: String,
    val partSix: String,
    val isUpdate: Boolean,
) {
    fun getEntireGoal(): EntireGoal = EntireGoal(
        level = targetLevel,
        startDate = Date(),//요기 서버에서 받아야야함
        endDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).parse(targetDate)!!,
    )

    fun getWeeklyGoal(): WeeklyGoal = WeeklyGoal(
        part1 = partOne.toInt(),
        part2 = partTwo.toInt(),
        part3 = partThree.toInt(),
        part4 = partFour.toInt(),
        part5 = partFive.toInt(),
        part6 = partSix.toInt(),
    )
}
