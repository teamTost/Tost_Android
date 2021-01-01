package com.tost.data.service.response

import com.tost.data.entity.MyNote

/**
 * Created By Malibin
 * on 1월 01, 2021
 */

data class MyNoteResponse(
    val part: String,
    val solvedQuestion: Int,
    val unsolvedQuestion: Int,
    val allQuestion: Int,
    val achieveRate: Int,
    val weekSolved: Int,
    val nextQuestionNum: Int,
) {
    fun toMyNote(): MyNote = MyNote(
        solvedProblemCount = solvedQuestion,
        remainProblemCount = unsolvedQuestion,
        wholeProblemCount = allQuestion,
        weeklyGoalRatio = achieveRate,
        weeklySolvedProblemCount = weekSolved,
        weeklyGoalCount = 10,
        nextProblemIndex = nextQuestionNum,
    )
}
