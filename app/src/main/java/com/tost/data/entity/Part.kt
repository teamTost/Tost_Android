package com.tost.data.entity

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.tost.R
import java.io.Serializable

enum class Part(
    val number: Int,
    @StringRes val knowHow: Int,
    @StringRes val corePoint: Int,
    @StringRes val howToSolve: Int,
    @StringRes val topic: Int,
    @StringRes val guideText: Int,
    @StringRes val questionNumbers: Int,
    @RawRes val guideAudio: Int,
) : Serializable {
    ONE(
        1,
        R.string.part1_know_how,
        R.string.part1_core_point,
        R.string.part1_how_to_solve,
        R.string.part1_topic,
        R.string.part1_directions,
        R.string.part1_question_numbers,
        R.raw.guide_part1
    ),
    TWO(
        2,
        R.string.part2_know_how,
        R.string.part2_core_point,
        R.string.part2_how_to_solve,
        R.string.part2_topic,
        R.string.part2_directions,
        R.string.part2_question_numbers,
        R.raw.guide_part2
    ),
    THREE(
        3,
        R.string.part3_know_how,
        R.string.part3_core_point,
        R.string.part3_how_to_solve,
        R.string.part3_topic,
        R.string.part3_directions,
        R.string.part3_question_numbers,
        R.raw.guide_part3
    ),
    FOUR(
        4,
        R.string.part4_know_how,
        R.string.part4_core_point,
        R.string.part4_how_to_solve,
        R.string.part4_topic,
        R.string.part4_directions,
        R.string.part4_question_numbers,
        R.raw.guide_part4
    ),
    FIVE(
        5,
        R.string.part5_know_how,
        R.string.part5_core_point,
        R.string.part5_how_to_solve,
        R.string.part5_topic,
        R.string.part5_directions,
        R.string.part5_question_numbers,
        R.raw.guide_part5
    ),
    SIX(
        6,
        R.string.part6_know_how,
        R.string.part6_core_point,
        R.string.part6_how_to_solve,
        R.string.part6_topic,
        R.string.part6_directions,
        R.string.part6_question_numbers,
        R.raw.guide_part6
    );

    private fun has(number: Int): Boolean = this.number == number

    companion object {
        fun findBy(number: Int): Part = values().find { it.has(number) }
            ?: throw IllegalArgumentException("Cannot find Part of $number")
    }
}
