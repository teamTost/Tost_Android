package com.tost.data.entity

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

data class Problem(
    val partNumber: Int,
    val questionNumber: Int,
    val passage: String,
    val imageUrl: String,
    val audioUrl: String,
    val subProblems: List<Problem>,
)
