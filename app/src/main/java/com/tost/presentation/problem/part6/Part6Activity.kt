package com.tost.presentation.problem.part6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.ProblemState
import com.tost.databinding.ActivityPart1Binding
import com.tost.databinding.ActivityPart6Binding
import com.tost.presentation.problem.ProblemGuideActivity
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.problem.part1.Part1ViewModel
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class Part6Activity : AudioBaseActivity(), AudioStateButton.OnClickListener {
    private var binding: ActivityPart6Binding? = null
    private val part6ViewModel: Part6ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart6Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        initView(binding)
        if (previousPermissionGranted()) onInitialPermissionGranted()
        else askAudioPermission()

        part6ViewModel.problem.observe(this) { startProblem() }
    }

    private fun initView(binding: ActivityPart6Binding) {
        binding.lifecycleOwner = this
        binding.viewModel = part6ViewModel
        binding.problemToolBar.setOnCloseClickListener { finish() }
        binding.buttonAudioController.setOnStateClickListener(this)
        binding.buttonRestart.setOnClickListener { cancelRecord() }
        binding.buttonSkip.setOnClickListener { skipPreparation() }
        binding.buttonNext.setOnClickListener { startNextProblem() }
        part6ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onInitialPermissionGranted() {
        part6ViewModel.loadProblem(getProblemNumber())
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> startRecord(RESPONSE_TIME)
        AudioStateButton.State.STOP -> cancelRecord()
        AudioStateButton.State.PLAYING -> part6ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part6ViewModel.pausePlayRecord()
    }

    private fun getProblemNumber(): Int =
        intent.getIntExtra(ProblemGuideActivity.KEY_PROBLEM_NUMBER, 1)

    private fun skipPreparation() {
        part6ViewModel.skipProgress()
        prepareNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startProblem() {
        rewindProgressBar(PREPARATION_TIME)
        playSound(prepareNoticePlayer) {
            playSound(beepPlayer) { part6ViewModel.startCountDown(PREPARATION_TIME) }
        }
        part6ViewModel.setOnProgressFinishListener { startReadingTime() }
    }

    private fun startReadingTime() {
        part6ViewModel.changeState(ProblemState.RESPONSE)
        rewindProgressBar(RESPONSE_TIME)
        playSound(readingNoticePlayer) {
            playSound(beepPlayer) { startRecord(RESPONSE_TIME) }
        }
    }

    private fun rewindProgressBar(maxProgress: Int) {
        requireBinding().progressBar.maxProgress = maxProgress
        part6ViewModel.resetProgress()
    }

    private fun startRecord(duration: Int) {
        part6ViewModel.prepareRecorder(getExternalDirectoryPath())
        part6ViewModel.startRecord(duration)
        part6ViewModel.setOnProgressFinishListener {
            part6ViewModel.finishRecord()
            playSound(beepPlayer)
            deployStopTalkingDialog()
        }
    }

    private fun deployStopTalkingDialog() {
        StopTalkingButtonsDialog(this).apply {
            setOnCheckProblemClickListener { listenMyRecord() }
            setOnNextClickListener { startNextProblem() }
        }.show()
//        part6ViewModel.saveSolvedProblem()
        //FIXME 나중에 푼 문제 저장되는것 주석 해제 할 것.
    }

    private fun listenMyRecord() {
        part6ViewModel.changeState(ProblemState.MY_RECORD)
        requireBinding().progressBar.initToProgressBar(part6ViewModel.getRecordDuration())
        requireBinding().progressBar.setOnProgressChangeListener { part6ViewModel.playRecord(it) }
        part6ViewModel.playRecord(0)
    }

    private fun cancelRecord() {
        part6ViewModel.cancelRecord()
        readingNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startNextProblem() {
        val intent = Intent(this, Part6Activity::class.java)
        intent.putExtra(ProblemGuideActivity.KEY_PROBLEM_NUMBER, getProblemNumber() + 1)
        startActivity(intent)
        finish()
    }

    private fun requireBinding() = binding ?: error("root view must be inflated")

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private val PREPARATION_TIME = TimeUnit.SECONDS.toMillis(3).toInt()
        private val RESPONSE_TIME = TimeUnit.SECONDS.toMillis(4).toInt()
    }
}
