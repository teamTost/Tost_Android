package com.tost.data.db

import androidx.room.TypeConverter
import com.tost.data.entity.Part
import com.tost.data.entity.Record
import java.util.*

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class TypeConverter {
    @TypeConverter
    fun toDate(milliseconds: Long?): Date? = milliseconds?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toPart(rawString: String): Part = Part.valueOf(rawString)

    @TypeConverter
    fun fromPart(part: Part?): String? = part?.name

    @TypeConverter
    fun toSubNumber(rawString: String): Record.SubNumber = Record.SubNumber.valueOf(rawString)

    @TypeConverter
    fun fromSubNumber(subNumber: Record.SubNumber?): String? = subNumber?.name
}
