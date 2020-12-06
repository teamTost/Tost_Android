package com.tost.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tost.data.entity.Record

/**
 * Created By Malibin
 * on 12월 06, 2020
 */


@Database(entities = [Record::class], version = 1)
abstract class TostDataBase : RoomDatabase() {


}