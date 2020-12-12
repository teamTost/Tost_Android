package com.tost.presentation.problem.part2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tost.data.dao.RecordsDao
import com.tost.data.entity.Part
import com.tost.data.entity.Record
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.ProblemState
import com.tost.presentation.problem.base.AudioViewModel
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class Part2ViewModel(
    recordsRepository: RecordsRepository,
) : AudioViewModel(recordsRepository) {

    constructor() : this(RecordsRepository(object : RecordsDao {
        override suspend fun insertRecord(record: Record) {
        }

        override suspend fun getRecordsOf(part: Part): List<Record> {
            return emptyList()
        }

        override suspend fun deleteRecord(record: Record) {
        }

        override suspend fun deleteAll() {
        }
    }))

    override val part: String = "part2"

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState>
        get() = _problemState

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }

    fun startPreparation(time: Int) = viewModelScope.launch {
        val tick = time.calculateTick()
        while (time > _progress.value ?: return@launch) {
            delay(tick)
            val currentProgress = _progress.value ?: return@launch
            _progress.postValue(currentProgress + tick.toInt())
            printLog(currentProgress.toString())
        }
    }
}
