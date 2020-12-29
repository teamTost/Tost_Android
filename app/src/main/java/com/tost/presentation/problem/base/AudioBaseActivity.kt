package com.tost.presentation.problem.base

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tost.R
import com.tost.presentation.utils.showToast

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

abstract class AudioBaseActivity : AppCompatActivity() {

    protected var prepareNoticePlayer: MediaPlayer? = null
    protected var readingNoticePlayer: MediaPlayer? = null
    protected var beepPlayer: MediaPlayer? = null

    abstract fun onInitialPermissionGranted()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAudio()
    }

    private fun initAudio() {
        prepareNoticePlayer = MediaPlayer.create(this, R.raw.begin_preparing_now)
        readingNoticePlayer = MediaPlayer.create(this, R.raw.begin_reading_aloud_now)
        beepPlayer = MediaPlayer.create(this, R.raw.beep)
    }

    override fun onDestroy() {
        super.onDestroy()
        prepareNoticePlayer?.release()
        readingNoticePlayer?.release()
        beepPlayer?.release()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_RECORD_PERMISSION) {
            val isPermissionGranted =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (isPermissionGranted) onInitialPermissionGranted()
            else showPermissionRejected()
        }
    }

    private fun showPermissionRejected() {
        showToast(R.string.audio_permission_rejected)
        finish()
    }

    protected fun askAudioPermission() {
        if (previousPermissionGranted()) return
        val permissionsToRequest = arrayOf(AUDIO_PERMISSION)
        requestPermissions(permissionsToRequest, REQUEST_CODE_RECORD_PERMISSION)
    }

    protected fun previousPermissionGranted(): Boolean {
        val previousPermissionGranted = ActivityCompat.checkSelfPermission(this, AUDIO_PERMISSION)
        return previousPermissionGranted == PackageManager.PERMISSION_GRANTED
    }

    protected fun getExternalDirectoryPath(): String = externalCacheDir?.absolutePath
        ?: throw IllegalStateException("Cannot get external Directory Path")

    protected fun playSound(mediaPlayer: MediaPlayer?, finishCallback: (() -> Unit)? = null) {
        mediaPlayer?.setOnCompletionListener {
            it.seekTo(0)
            finishCallback?.invoke()
        }
        mediaPlayer?.start()
    }

    companion object {
        const val REQUEST_CODE_RECORD_PERMISSION = 1000
        private const val AUDIO_PERMISSION = android.Manifest.permission.RECORD_AUDIO
    }
}
