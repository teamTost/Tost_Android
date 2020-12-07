package com.tost.data.dao

import androidx.room.*
import com.tost.data.entity.Part
import com.tost.data.entity.Record

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

@Dao
interface RecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Query("SELECT * FROM record WHERE part = :part ORDER BY saveDate DESC")
    suspend fun getRecordsOf(part: Part): List<Record>

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("DELETE FROM record")
    suspend fun deleteAll()
}
