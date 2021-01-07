package com.tost.presentation.problem.part1

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.ProblemState
import com.tost.databinding.ActivityPart1Binding
import com.tost.presentation.problem.ProblemGuideActivity.Companion.KEY_PROBLEM_NUMBER
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.problem.part2.Part2Activity
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class Part1Activity : AudioBaseActivity(), AudioStateButton.OnClickListener {
    private var binding: ActivityPart1Binding? = null
    private val part1ViewModel: Part1ViewModel by viewModels()

    private var readingNoticePlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        initView(binding)
        if (previousPermissionGranted()) onInitialPermissionGranted()
        else askAudioPermission()

        part1ViewModel.problem.observe(this) { startProblem() }
    }

    private fun initView(binding: ActivityPart1Binding) {
        readingNoticePlayer = MediaPlayer.create(this, R.raw.begin_reading_aloud_now)
        binding.lifecycleOwner = this
        binding.viewModel = part1ViewModel
        binding.problemToolBar.setOnCloseClickListener { finish() }
        binding.buttonAudioController.setOnStateClickListener(this)
        binding.buttonRestart.setOnClickListener { cancelRecord() }
        binding.buttonSkip.setOnClickListener { skipPreparation() }
        binding.buttonNext.setOnClickListener { startNextProblem() }
        part1ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onInitialPermissionGranted() {
        part1ViewModel.loadProblem(getProblemNumber())
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> startRecord(RESPONSE_TIME)
        AudioStateButton.State.STOP -> cancelRecord()
        AudioStateButton.State.PLAYING -> part1ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part1ViewModel.pausePlayRecord()
    }

    private fun getProblemNumber(): Int = intent.getIntExtra(KEY_PROBLEM_NUMBER, 1)

    private fun skipPreparation() {
        part1ViewModel.skipProgress()
        prepareNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startProblem() {
        rewindProgressBar(PREPARATION_TIME)
        playSound(prepareNoticePlayer) {
            playSound(beepPlayer) { part1ViewModel.startCountDown(PREPARATION_TIME) }
        }
        part1ViewModel.setOnProgressFinishListener { startReadingTime() }
    }

    private fun startReadingTime() {
        part1ViewModel.changeState(ProblemState.RESPONSE)
        rewindProgressBar(RESPONSE_TIME)
        playSound(readingNoticePlayer) {
            playSound(beepPlayer) { startRecord(RESPONSE_TIME) }
        }
    }

    private fun rewindProgressBar(maxProgress: Int) {
        requireBinding().progressBar.maxProgress = maxProgress
        part1ViewModel.resetProgress()
    }

    private fun startRecord(duration: Int) {
        part1ViewModel.prepareRecorder(getExternalDirectoryPath())
        part1ViewModel.startRecord(duration)
        part1ViewModel.setOnProgressFinishListener {
            part1ViewModel.finishRecord()
            playSound(beepPlayer)
            deployStopTalkingDialog()
        }
    }

    private fun deployStopTalkingDialog() {
        StopTalkingButtonsDialog(this).apply {
            setOnCheckProblemClickListener { listenMyRecord() }
            setOnNextClickListener { startNextProblem() }
        }.show()
//        part1ViewModel.saveSolvedProblem()
        //FIXME 나중에 푼 문제 저장되는것 주석 해제 할 것.
    }

    private fun listenMyRecord() {
        part1ViewModel.changeState(ProblemState.MY_RECORD)
        requireBinding().progressBar.initToProgressBar(part1ViewModel.getRecordDuration())
        requireBinding().progressBar.setOnProgressChangeListener { part1ViewModel.playRecord(it) }
        part1ViewModel.playRecord(0)
    }

    private fun cancelRecord() {
        part1ViewModel.cancelRecord()
        readingNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startNextProblem() {
        val intent = Intent(this, Part1Activity::class.java)
        intent.putExtra(KEY_PROBLEM_NUMBER, getProblemNumber() + 1)
        startActivity(intent)
        finish()
    }

    private fun requireBinding() = binding ?: error("root view must be inflated")

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        readingNoticePlayer?.release()
    }

    companion object {
        private val PREPARATION_TIME = TimeUnit.SECONDS.toMillis(3).toInt()
        private val RESPONSE_TIME = TimeUnit.SECONDS.toMillis(4).toInt()
    }
}
