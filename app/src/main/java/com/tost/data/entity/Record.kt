package com.tost.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

@Entity
data class Record(
    @PrimaryKey
    val id: String,
    val filePath: String,
    val part: Part,
    val saveDate: Date,
    val subNumber: SubNumber = SubNumber.ONE
) {
    enum class SubNumber {
        ONE, TWO, THREE;
    }
}