package com.tost.presentation.problem.part2

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.Part
import com.tost.data.entity.ProblemState
import com.tost.databinding.ActivityPart2Binding
import com.tost.presentation.problem.ProblemGuideActivity
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.problem.part1.Part1Activity
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class Part2Activity : AudioBaseActivity(), AudioStateButton.OnClickListener {
    private var binding: ActivityPart2Binding? = null
    private val part2ViewModel: Part2ViewModel by viewModels()

    private var readingNoticePlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        initView(binding)
        if (previousPermissionGranted()) onInitialPermissionGranted()
        else askAudioPermission()

        part2ViewModel.problem.observe(this) { startProblem() }
    }

    private fun initView(binding: ActivityPart2Binding) {
        readingNoticePlayer = MediaPlayer.create(this, R.raw.begin_reading_aloud_now)
        binding.lifecycleOwner = this
        binding.viewModel = part2ViewModel
        binding.problemToolBar.setOnCloseClickListener { finish() }
        binding.buttonAudioController.setOnStateClickListener(this)
        binding.buttonRestart.setOnClickListener { cancelRecord() }
        binding.buttonSkip.setOnClickListener { skipPreparation() }
        binding.buttonNext.setOnClickListener { startNextProblem() }
        part2ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onInitialPermissionGranted() {
        part2ViewModel.loadProblem(getProblemNumber())
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> startRecord(RESPONSE_TIME)
        AudioStateButton.State.STOP -> cancelRecord()
        AudioStateButton.State.PLAYING -> part2ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part2ViewModel.pausePlayRecord()
    }

    private fun getProblemNumber(): Int =
        intent.getIntExtra(ProblemGuideActivity.KEY_PROBLEM_NUMBER, 1)

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
            part2ViewModel.saveRecord()
            playSound(beepPlayer)
            deployStopTalkingDialog()
        }
    }

    private fun deployStopTalkingDialog() {
        StopTalkingButtonsDialog(this).apply {
            setOnCheckProblemClickListener { listenMyRecord() }
            setOnNextClickListener { startNextProblem() }
        }.show()
        //        part2ViewModel.saveSolvedProblem()
        //FIXME 나중에 푼 문제 저장되는것 주석 해제 할 것.
    }

    private fun listenMyRecord() {
        part2ViewModel.preparePlayingRecord()
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

    private fun startNextProblem() {
        val intent = Intent(this, Part2Activity::class.java)
        intent.putExtra(ProblemGuideActivity.KEY_PROBLEM_NUMBER, getProblemNumber() + 1)
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
}//TODO 아예 AudioViewModel 도 부모클래스에다가 집어넣어도 될거같은데
