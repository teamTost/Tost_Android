package com.tost.data.entity

import androidx.annotation.StringRes
import com.tost.R
import java.io.Serializable

enum class Part(
    val number: Int,
    @StringRes val knowHow: Int,
    @StringRes val corePoint: Int,
    @StringRes val howToSolve: Int,
) : Serializable {

    ONE(1, R.string.part1_know_how, R.string.part1_core_point, R.string.part1_how_to_solve),
    TWO(2, R.string.part2_know_how, R.string.part2_core_point, R.string.part2_how_to_solve),
    THREE(3, R.string.part3_know_how, R.string.part3_core_point, R.string.part3_how_to_solve),
    FOUR(4, R.string.part4_know_how, R.string.part4_core_point, R.string.part4_how_to_solve),
    FIVE(5, R.string.part5_know_how, R.string.part5_core_point, R.string.part5_how_to_solve),
    SIX(6, R.string.part6_know_how, R.string.part6_core_point, R.string.part6_how_to_solve);

    private fun has(number: Int): Boolean = this.number == number

    companion object {
        fun findBy(number: Int): Part = values().find { it.has(number) }
            ?: throw IllegalArgumentException("Cannot find Part of $number")
    }
}