package com.tost.data.repository

import com.tost.data.dao.RecordsDao
import com.tost.data.entity.Part
import com.tost.data.entity.Record
import java.io.File

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class RecordsRepository(
    private val recordsDao: RecordsDao,
) {
    suspend fun getRecordsOf(part: Part): List<Record> {
        return recordsDao.getRecordsOf(part)
    }

    fun deleteRecord(fileName: String) {
        File(fileName).delete()
    }

    suspend fun deleteRecord(record: Record) {
        File(record.filePath).delete()
        recordsDao.deleteRecord(record)
    }

    suspend fun deleteAllRecords() {
        recordsDao.deleteAll()
    }
}