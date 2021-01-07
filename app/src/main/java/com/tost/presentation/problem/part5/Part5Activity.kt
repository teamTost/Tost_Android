package com.tost.presentation.problem.part5

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.tost.data.entity.ProblemState
import com.tost.databinding.ActivityPart5Binding
import com.tost.presentation.problem.ProblemGuideActivity
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.problem.part6.Part6Activity
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class Part5Activity : AudioBaseActivity(), AudioStateButton.OnClickListener {
    private var binding: ActivityPart5Binding? = null
    private val part5ViewModel: Part5ViewModel by viewModels()

    private var problemPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        initView(binding)
        if (previousPermissionGranted()) onInitialPermissionGranted()
        else askAudioPermission()

        part5ViewModel.problem.observe(this) { startProblem() }
    }

    private fun initView(binding: ActivityPart5Binding) {
        binding.lifecycleOwner = this
        binding.viewModel = part5ViewModel
        binding.problemToolBar.setOnCloseClickListener { finish() }
        binding.buttonAudioController.setOnStateClickListener(this)
        binding.buttonRestart.setOnClickListener { cancelRecord() }
        binding.buttonSkip.setOnClickListener { skipPreparation() }
        binding.buttonNext.setOnClickListener { startNextProblem() }
        part5ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onInitialPermissionGranted() {
        part5ViewModel.loadProblem(getProblemNumber())
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> startRecord(RESPONSE_TIME)
        AudioStateButton.State.STOP -> cancelRecord()
        AudioStateButton.State.PLAYING -> part5ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part5ViewModel.pausePlayRecord()
    }

    private fun getProblemNumber(): Int =
        intent.getIntExtra(ProblemGuideActivity.KEY_PROBLEM_NUMBER, 1)

    private fun skipPreparation() {
        part5ViewModel.skipProgress()
        prepareNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startProblem() {
        problemPlayer = MediaPlayer().apply {
//            setDataSource(part5ViewModel.getProblemSoundUrl())
            prepare()
            setOnCompletionListener { startPreparation() }
        }
        problemPlayer?.start()
    }

    private fun startPreparation() {
//        binding?.zoneTimer?.visibility = View.VISIBLE
        rewindProgressBar(PREPARATION_TIME)
        playSound(prepareNoticePlayer) {
            playSound(beepPlayer) { part5ViewModel.startCountDown(PREPARATION_TIME) }
        }
        part5ViewModel.setOnProgressFinishListener { startReadingTime() }
    }

    private fun startReadingTime() {
        part5ViewModel.changeState(ProblemState.RESPONSE)
        rewindProgressBar(RESPONSE_TIME)
        playSound(readingNoticePlayer) {
            playSound(beepPlayer) { startRecord(RESPONSE_TIME) }
        }
    }

    private fun rewindProgressBar(maxProgress: Int) {
        requireBinding().progressBar.maxProgress = maxProgress
        part5ViewModel.resetProgress()
    }

    private fun startRecord(duration: Int) {
        part5ViewModel.prepareRecorder(getExternalDirectoryPath())
        part5ViewModel.startRecord(duration)
        part5ViewModel.setOnProgressFinishListener {
            part5ViewModel.finishRecord()
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
        part5ViewModel.changeState(ProblemState.MY_RECORD)
        requireBinding().progressBar.initToProgressBar(part5ViewModel.getRecordDuration())
        requireBinding().progressBar.setOnProgressChangeListener { part5ViewModel.playRecord(it) }
        part5ViewModel.playRecord(0)
    }

    private fun cancelRecord() {
        part5ViewModel.cancelRecord()
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
        problemPlayer?.release()
        problemPlayer = null
    }

    companion object {
        private val PREPARATION_TIME = TimeUnit.SECONDS.toMillis(3).toInt()
        private val RESPONSE_TIME = TimeUnit.SECONDS.toMillis(4).toInt()
    }
}
