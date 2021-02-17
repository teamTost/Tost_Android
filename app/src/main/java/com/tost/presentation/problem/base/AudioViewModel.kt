package com.tost.presentation.problem.base

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.Part
import com.tost.data.entity.Problem
import com.tost.data.entity.Record
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.TostRecorder
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.BaseViewModel
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.*
import java.util.*

/**
 * Created By Malibin
 * on 12월 12, 2020
 */

abstract class AudioViewModel constructor(
    private val recordsRepository: RecordsRepository,
) : BaseViewModel() {

    abstract val part: Part

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int>
        get() = _progress

    private val _audioState = MutableLiveData(AudioStateButton.State.STOP)
    val audioState: LiveData<AudioStateButton.State>
        get() = _audioState

    private val cachedRecords: EnumMap<Problem.SubNumber, Record> =
        EnumMap(Problem.SubNumber::class.java)

    private val recordPlayer = MediaPlayer()
    private val tostRecorder = TostRecorder()
    private var isRecordPlayFinished: Boolean = false

    private var progressJob: Job? = null
    private var onProgressFinishListener: OnProgressFinishListener? = null

    fun setOnProgressFinishListener(l: OnProgressFinishListener?) {
        this.onProgressFinishListener = l
    }

    fun prepareRecorder(
        baseFilePath: String,
        subProblemNumber: Problem.SubNumber = Problem.SubNumber.ONE
    ) {
        val fileName = "$baseFilePath${part.name}_${subProblemNumber.name}"
        tostRecorder.prepare(fileName)
    }

    fun startRecord(duration: Int) {
        tostRecorder.start()
        startCountDown(duration)
        _audioState.value = AudioStateButton.State.STOP
    }

    fun startCountDown(duration: Int) {
        progressJob = viewModelScope.launch {
            resetProgress()
            val tick = duration.calculateTick()
            while (duration > getCurrentProgress()) {
                delay(tick)
                _progress.postValue(getCurrentProgress() + tick.toInt())
            }
            onProgressFinishListener?.onFinish()
        }
    }

    fun skipProgress() {
        progressJob?.cancel()
        onProgressFinishListener?.onFinish()
    }

    fun saveRecord(subProblemNumber: Problem.SubNumber = Problem.SubNumber.ONE) {
        tostRecorder.stop()
        tostRecorder.reset()
        viewModelScope.launch {
            val record = Record(
                filePath = tostRecorder.fileName,
                part = part,
                subNumber = subProblemNumber,
            )
            recordsRepository.saveRecord(record)
            cachedRecords[subProblemNumber] = record
        }
    }

    fun preparePlayingRecord(
        subProblemNumber: Problem.SubNumber = Problem.SubNumber.ONE
    ) = runBlocking {
        printLog(cachedRecords.toString())
        if (cachedRecords[subProblemNumber] == null) loadAllRecords()
        val cachedRecord = cachedRecords[subProblemNumber]
            ?: error("cannot find record of $subProblemNumber")

        recordPlayer.reset()
        recordPlayer.setDataSource(cachedRecord.filePath)
        recordPlayer.prepare()
    }

    private suspend fun loadAllRecords() {
        val records = recordsRepository.getRecordsOf(part).also { printLog(it.toString()) }
        records.forEach { cachedRecords[it.subNumber] = it }
        printLog(cachedRecords.toString())
    }

    fun cancelRecord() {
        progressJob?.cancel()
        tostRecorder.stop()
        tostRecorder.reset()
        resetProgress()
        _audioState.value = AudioStateButton.State.RECORDING
        recordsRepository.deleteRecord(tostRecorder.fileName)
    }

    @JvmOverloads
    fun playRecord(duration: Int = if (isRecordPlayFinished) 0 else getCurrentProgress()) {
        progressJob?.cancel()
        recordPlayer.seekTo(duration)
        recordPlayer.start()
        isRecordPlayFinished = false
        _audioState.value = AudioStateButton.State.PAUSE
        progressJob = viewModelScope.launch {
            val tick = recordPlayer.duration.calculateTick()
            while (recordPlayer.isPlaying) {
                delay(tick)
                _progress.postValue(recordPlayer.currentPosition)
            }
            _audioState.value = AudioStateButton.State.PLAYING
            isRecordPlayFinished = true
        }
    }

    private fun getCurrentProgress() = progress.value
        ?: throw IllegalStateException("progress value cannot be null")

    private fun Int.calculateTick(): Long = 10L + (this shr 10)

    fun getRecordDuration() = recordPlayer.duration

    fun resetProgress() = _progress.postValue(0)

    fun pausePlayRecord() {
        recordPlayer.pause()
        progressJob?.cancel()
        _audioState.value = AudioStateButton.State.PLAYING
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.IO).launch {
            recordsRepository.deleteAllRecords()
        }
        recordPlayer.release()
        tostRecorder.release()
    }

    fun interface OnProgressFinishListener {
        fun onFinish()
    }
}
