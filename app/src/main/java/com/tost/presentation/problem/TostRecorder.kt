package com.tost.presentation.problem

import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Created By Malibin
 * on 12월 06, 2020
 */

class TostRecorder(
    private val recorder: MediaRecorder = MediaRecorder(),
) {
    fun prepare(fileName: String) {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setOutputFile("$fileName$FILE_EXTENSION")
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.prepare()
    }

    fun start() = recorder.start()

    @RequiresApi(Build.VERSION_CODES.N)
    fun pause() = recorder.pause()

    @RequiresApi(Build.VERSION_CODES.N)
    fun resume() = recorder.resume()

    fun stop() {
        recorder.stop()
        recorder.release()
    }

    fun release() = recorder.release()

    companion object {
        private const val FILE_EXTENSION = ".3gp"
    }
}
