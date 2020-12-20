package com.tost.presentation.problem

import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class TostRecorder(
    private var recorder: MediaRecorder? = MediaRecorder(),
) {
    var fileName: String = ""
        private set

    var isStarted: Boolean = false
        private set

    @Synchronized
    fun prepare(fileName: String) {
        this.fileName = "$fileName$FILE_EXTENSION"
        this.release()
        recorder = MediaRecorder()
        requireRecorder().setAudioSource(MediaRecorder.AudioSource.MIC)
        requireRecorder().setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        requireRecorder().setOutputFile(this.fileName)
        requireRecorder().setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        requireRecorder().prepare()
    }

    fun start() {
        requireRecorder().start()
        isStarted = true
    }

    fun stop() {
        if (isStarted) requireRecorder().stop()
    }

    fun reset() = requireRecorder().reset()

    fun release() {
        recorder?.release()
        recorder = null
    }

    private fun requireRecorder() = recorder
        ?: throw IllegalStateException("MediaRecorder not initialized")

    companion object {
        private const val FILE_EXTENSION = ".3gp"
    }
}
