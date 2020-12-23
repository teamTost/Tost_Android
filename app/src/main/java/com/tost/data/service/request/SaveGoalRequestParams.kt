package com.tost.data.service.request

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class SaveGoalRequestParams(
    goalDate: Date,
    val targetLevel: Int,
) {
    val targetDate: String = DATE_FORMAT.format(goalDate)

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    }
}
