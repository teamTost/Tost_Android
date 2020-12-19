package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.widget.AudioStateButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        initView(binding)
        if (previousPermissionGranted()) onInitialPermissionGranted()
        else askAudioPermission()
    }

    private fun initView(binding: ActivityPart2Binding) {
        binding.lifecycleOwner = this
        binding.part = Part.TWO
        binding.imageUrl = ""
        binding.viewModel = part2ViewModel
        binding.buttonAudioController.setOnStateClickListener(this)
        part2ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onInitialPermissionGranted() {
        initAudio()
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

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> part2ViewModel.startRecord()
        AudioStateButton.State.STOP -> part2ViewModel.cancelRecord()
        AudioStateButton.State.PLAYING -> part2ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part2ViewModel.pausePlayRecord()
    }

    private fun startProblem(binding: ActivityPart2Binding) {
        binding.progressBar.maxProgress = 3000
        part2ViewModel.setOnProgressFinishListener { startReadingTime(binding) }
        prepareNoticePlayer?.setOnCompletionListener {
            beepPlayer?.setOnCompletionListener {
                it.seekTo(0)
                part2ViewModel.startCountDown(binding.progressBar.maxProgress)
            }
            beepPlayer?.start()
        }
        prepareNoticePlayer?.start()
    }

    private fun startReadingTime(binding: ActivityPart2Binding) {
        part2ViewModel.setOnProgressFinishListener {
            finishProblem()
        }
        readingNoticePlayer?.setOnCompletionListener {
            beepPlayer?.setOnCompletionListener {
                it.seekTo(0)
                part2ViewModel.startCountDown(4000)
            }
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
