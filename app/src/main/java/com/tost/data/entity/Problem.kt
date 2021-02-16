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
) {
    fun getSubProblem(subNumber: SubNumber): Problem {
        require(subNumber != SubNumber.FINISH) { "Do not exist Problem of $subNumber" }
        return subProblems[subNumber.index]
    }

    enum class SubNumber(val index: Int) {
        ONE(0),
        TWO(1),
        THREE(2),
        FINISH(-1);

        fun next(): SubNumber = when (this) {
            ONE -> TWO
            TWO -> THREE
            else -> FINISH
        }
    }
}
