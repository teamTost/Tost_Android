package com.tost.presentation.problem

import androidx.annotation.StringRes
import com.tost.R

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

enum class ProblemState(
    @StringRes val displayName: Int
) {
    PREPARE(R.string.preparation_time),
    RESPONSE(R.string.response_time),
    MY_RECORD(R.string.my_record);
}
