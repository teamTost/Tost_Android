package com.tost.data.entity

/**
 * Created By Malibin
 * on 11월 26, 2020
 */

data class MyNote(
    val solvedProblemCount: Int,
    val remainProblemCount: Int,
    val wholeProblemCount: Int,
    val weeklyGoalRatio: Int,
    val weeklySolvedProblemCount: Int,
    val weeklyGoalCount: Int,
    val nextProblemIndex: Int,
)
