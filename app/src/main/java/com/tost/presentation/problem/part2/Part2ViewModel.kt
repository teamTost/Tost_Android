package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tost.R
import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.ProblemState
import com.tost.presentation.problem.TostRecorder
import com.tost.presentation.problem.widget.AudioStateButton

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class Part2ViewModel(
    private val recordsRepository: RecordsRepository,
) : ViewModel() {

    private val _problemState = MutableLiveData(ProblemState.PREPARE)
    val problemState: LiveData<ProblemState>
        get() = _problemState

    private val _audioState = MutableLiveData(AudioStateButton.State.RECORDING)
    val audioState: LiveData<AudioStateButton.State>
        get() = _audioState

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    private val tostRecorder = TostRecorder()
    private val recordPlayer = MediaPlayer()

    fun changeState(state: ProblemState) {
        _problemState.value = state
    }

    fun prepareRecorder(baseFilePath: String) {
        tostRecorder.prepare("$baseFilePath$PART")
    }

    fun startRecord() {
        if (audioState.value == AudioStateButton.State.PAUSE) {
            resumeRecord()
            return
        }
        tostRecorder.start()
        _audioState.value = AudioStateButton.State.RECORDING
    }

    fun pauseRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tostRecorder.pause()
            _audioState.value = AudioStateButton.State.PAUSE
            return
        }
        _toastMessage.value = R.string.cannot_pause_record
    }

    private fun resumeRecord() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        tostRecorder.resume()
        _audioState.value = AudioStateButton.State.RECORDING
    }

    @JvmOverloads
    fun playRecord(duration: Int = 0) {
        recordPlayer.seekTo(duration)
        recordPlayer.start()
        _audioState.value = AudioStateButton.State.PLAYING
    }

    fun pausePlayRecord() {
        recordPlayer.pause()
        _audioState.value = AudioStateButton.State.PAUSE
    }

    override fun onCleared() {
        super.onCleared()
        tostRecorder.release()
        recordPlayer.release()
    }

    companion object {
        private const val PART = "part2"
    }
}
