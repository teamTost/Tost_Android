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
    val userIdx: String?,
    val targetDate: String?,
    val targetLevel: Int?,
    val partOne: String?,
    val partTwo: String?,
    val partThree: String?,
    val partFour: String?,
    val partFive: String?,
    val partSix: String?,
    val isUpdate: Boolean?,
) {
    fun getEntireGoal(): EntireGoal? {
        return EntireGoal(
            level = targetLevel ?: return null,
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                .parse("2020-09-01")!!,//요기 서버에서 받아야야함
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                .parse(targetDate ?: return null)!!,
        )
    }

    fun getWeeklyGoal(): WeeklyGoal? {
        return WeeklyGoal(
            part1 = partOne?.toInt() ?: return null,
            part2 = partTwo?.toInt() ?: return null,
            part3 = partThree?.toInt() ?: return null,
            part4 = partFour?.toInt() ?: return null,
            part5 = partFive?.toInt() ?: return null,
            part6 = partSix?.toInt() ?: return null,
        )
    }
}
