package com.tost.presentation.problem.base

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tost.R

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

abstract class AudioBaseActivity : AppCompatActivity() {

    abstract fun onInitialPermissionGranted()

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
        showToast( R.string.audio_permission_rejected)
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

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(@StringRes stringResId: Int) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val REQUEST_CODE_RECORD_PERMISSION = 1000
        private const val AUDIO_PERMISSION = android.Manifest.permission.RECORD_AUDIO
    }
}

// TODO 아 BASE 존나 만들기 싫었는데.. 문제는 전부 Fragment로, 시험과 연습을 각각 액티비티로 하는게 가장 좋은 설계인 것 같다.
