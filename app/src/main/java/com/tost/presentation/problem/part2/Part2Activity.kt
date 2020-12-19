package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding
import com.tost.presentation.problem.ProblemState
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.printLog
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

        part2ViewModel.audioState.observe(this){
            printLog("viewmodel : $it button : ${binding.buttonAudioController.state}")
        }
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
        startProblem()
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

    private fun startProblem() {
        rewindProgressBar(3000)
        part2ViewModel.setOnProgressFinishListener { startReadingTime() }
        playSound(prepareNoticePlayer) {
            playSound(beepPlayer) { part2ViewModel.startCountDown(3000) }
        }
    }

    private fun startReadingTime() {
        part2ViewModel.changeState(ProblemState.RESPONSE)
        rewindProgressBar(4000)
        playSound(readingNoticePlayer) {
            playSound(beepPlayer) {
                startRecord(4000)
                part2ViewModel.startCountDown(4000)
            }
        }
    }

    private fun startRecord(duration: Int) {
        part2ViewModel.startRecord()
        part2ViewModel.setOnProgressFinishListener {
            part2ViewModel.finishRecord()
            finishProblem()
        }
    }

    private fun rewindProgressBar(maxProgress: Int) {
        requireBinding().progressBar.maxProgress = maxProgress
        part2ViewModel.resetProgress()
    }

    private fun playSound(mediaPlayer: MediaPlayer?, finishCallback: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            it.seekTo(0)
            finishCallback()
        }
        mediaPlayer?.start()
    }

    private fun finishProblem() {
        part2ViewModel.changeState(ProblemState.MY_RECORD)
        playSound(beepPlayer) {
            showToast("STOP TALKING")
            requireBinding().progressBar.initToProgressBar(part2ViewModel.getRecordDuration())
            requireBinding().progressBar.setOnProgressChangeListener { part2ViewModel.playRecord(it) }
            part2ViewModel.playRecord(0)
        }
    }

    private fun requireBinding() = binding
        ?: throw IllegalStateException("root view must be inflated")

    override fun onDestroy() {
        super.onDestroy()
        prepareNoticePlayer?.release()
        readingNoticePlayer?.release()
        beepPlayer?.release()
        binding = null
    }
}
