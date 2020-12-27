package com.tost.data.service.response

import com.tost.data.entity.Problem

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

data class ProblemResponse(
    val idx: Int,
    val part: String,
    val questionNum: String,
    val smallQuestionNum: String,
    val img: String,
    val question: String,
    val voice: String,
)

fun List<ProblemResponse>.toProblem(): Problem {
    return Problem(
        partNumber = this[0].part.toInt(),
        questionNumber = this[0].questionNum.toInt(),
        passage = this[0].question,
        imageUrl = this[0].img,
        audioUrl = this[0].voice,
        subProblems = this.drop(1)
            .map {
                Problem(
                    partNumber = it.part.toInt(),
                    questionNumber = it.questionNum.toInt(),
                    passage = it.question,
                    imageUrl = it.img,
                    audioUrl = it.voice,
                    subProblems = emptyList(),
                )
            },
    )
}
