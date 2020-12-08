package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.problem.widget.TostProgressBar
import com.tost.presentation.utils.printLog

class Part2Activity : AudioBaseActivity(), AudioStateButton.OnClickListener {

    private var prepareNoticePlayer: MediaPlayer? = null
    private var readingNoticePlayer: MediaPlayer? = null
    private var beepPlayer: MediaPlayer? = null

    private var binding: ActivityPart2Binding? = null

    private val part2ViewModel: Part2ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        initAudio()
        initView(binding)
        if (previousPermissionGranted()) startProblem(binding)
        else askAudioPermission()
    }

    override fun onInitialPermissionGranted() {
        startProblem(binding ?: throw IllegalStateException("root view must be inflated"))
    }

    private fun initAudio() {
        prepareNoticePlayer = MediaPlayer.create(this, R.raw.begin_preparing_now)
        readingNoticePlayer = MediaPlayer.create(this, R.raw.begin_reading_aloud_now)
        beepPlayer = MediaPlayer.create(this, R.raw.beep)
        part2ViewModel.prepareRecorder(getExternalDirectoryPath())
    }

    private fun getExternalDirectoryPath(): String = externalCacheDir?.absolutePath
        ?: throw IllegalStateException("Cannot get external Directory Path")

    private fun initView(binding: ActivityPart2Binding) {
        binding.lifecycleOwner = this
        binding.part = Part.TWO
        binding.imageUrl = ""
        binding.buttonAudioController.setOnStateClickListener(this)
        part2ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> part2ViewModel.startRecord()
        AudioStateButton.State.STOP -> part2ViewModel.pauseRecord()
        AudioStateButton.State.PLAYING -> part2ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part2ViewModel.pausePlayRecord()
    }

    private fun startProblem(binding: ActivityPart2Binding) {
        binding.progressBar.setOnProgressFinishListener { startReadingTime(binding) }
        prepareNoticePlayer?.setOnCompletionListener {
            beepPlayer?.setOnCompletionListener { binding.progressBar.start(); it.seekTo(0) }
            beepPlayer?.start()
        }
        prepareNoticePlayer?.start()
    }

    private fun startReadingTime(binding: ActivityPart2Binding) {
        binding.progressBar.setOnProgressFinishListener { finishProblem() }
        readingNoticePlayer?.setOnCompletionListener {
            beepPlayer?.setOnCompletionListener { binding.progressBar.start(); it.seekTo(0) }
            beepPlayer?.start()
        }
        readingNoticePlayer?.start()
    }

    private fun finishProblem() {
        beepPlayer?.start()
        beepPlayer?.setOnCompletionListener { showToast("STOP TALKING") }
    }

    override fun onDestroy() {
        super.onDestroy()
        prepareNoticePlayer?.release()
        readingNoticePlayer?.release()
        beepPlayer?.release()
        binding = null
    }
}

// 지금 일단 뷰모델 로직은 다 괜찮은데,
// progressbar랑 실제 녹음 / 이런게 싱크고 뭐고 아무것도 안맞음. ㅠㅠ
