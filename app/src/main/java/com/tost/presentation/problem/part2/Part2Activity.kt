package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding
import com.tost.data.entity.ProblemState
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

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
        binding.buttonRestart.setOnClickListener { cancelRecord() }
        binding.buttonSkip.setOnClickListener { skipPreparation() }
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
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> startRecord(RESPONSE_TIME)
        AudioStateButton.State.STOP -> cancelRecord()
        AudioStateButton.State.PLAYING -> part2ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part2ViewModel.pausePlayRecord()
    }

    private fun skipPreparation() {
        part2ViewModel.skipProgress()
        prepareNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startProblem() {
        rewindProgressBar(PREPARATION_TIME)
        playSound(prepareNoticePlayer) {
            playSound(beepPlayer) { part2ViewModel.startCountDown(PREPARATION_TIME) }
        }
        part2ViewModel.setOnProgressFinishListener { startReadingTime() }
    }

    private fun startReadingTime() {
        part2ViewModel.changeState(ProblemState.RESPONSE)
        rewindProgressBar(RESPONSE_TIME)
        playSound(readingNoticePlayer) {
            playSound(beepPlayer) { startRecord(RESPONSE_TIME) }
        }
    }

    private fun rewindProgressBar(maxProgress: Int) {
        requireBinding().progressBar.maxProgress = maxProgress
        part2ViewModel.resetProgress()
    }

    private fun startRecord(duration: Int) {
        part2ViewModel.prepareRecorder(getExternalDirectoryPath())
        part2ViewModel.startRecord(duration)
        part2ViewModel.setOnProgressFinishListener {
            part2ViewModel.finishRecord()
            playSound(beepPlayer)
            deployStopTalkingDialog()
        }
    }

    private fun deployStopTalkingDialog() {
        StopTalkingButtonsDialog(this).apply {
            setOnCheckProblemClickListener { listenMyRecord() }
            setOnNextClickListener {}
        }.show()
    }

    private fun listenMyRecord() {
        part2ViewModel.changeState(ProblemState.MY_RECORD)
        requireBinding().progressBar.initToProgressBar(part2ViewModel.getRecordDuration())
        requireBinding().progressBar.setOnProgressChangeListener { part2ViewModel.playRecord(it) }
        part2ViewModel.playRecord(0)
    }

    private fun cancelRecord() {
        part2ViewModel.cancelRecord()
        readingNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
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

    companion object {
        private val PREPARATION_TIME = TimeUnit.SECONDS.toMillis(3).toInt()
        private val RESPONSE_TIME = TimeUnit.SECONDS.toMillis(4).toInt()
    }
}//TODO 아예 AudioViewModel 도 부모클래스에다가 집어넣어도 될거같은데
