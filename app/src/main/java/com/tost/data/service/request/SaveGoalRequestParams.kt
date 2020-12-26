package com.tost.data.service.request

import com.tost.data.entity.EntireGoal
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class SaveGoalRequestParams(entireGoal: EntireGoal) {
    val targetLevel: Int = entireGoal.level
    val targetDate: String = DATE_FORMAT.format(entireGoal.endDate)

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    }
}
