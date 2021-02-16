package com.tost.presentation.problem.part3

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.tost.R
import com.tost.data.entity.ProblemState
import com.tost.databinding.ActivityPart3Binding
import com.tost.presentation.problem.ProblemGuideActivity
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.dialog.StopTalkingButtonsDialog
import com.tost.presentation.problem.dialog.StopTalkingPauseDialog
import com.tost.presentation.problem.part6.Part6Activity
import com.tost.presentation.problem.widget.AudioStateButton
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class Part3Activity : AudioBaseActivity(), AudioStateButton.OnClickListener {
    private var binding: ActivityPart3Binding? = null
    private val part3ViewModel: Part3ViewModel by viewModels()

    private var problemPlayer: MediaPlayer? = null
    private var speakingNoticePlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        this.binding = binding

        initView(binding)
        if (previousPermissionGranted()) onInitialPermissionGranted()
        else askAudioPermission()

        part3ViewModel.problem.observe(this) { startProblem() }
    }

    private fun initView(binding: ActivityPart3Binding) {
        speakingNoticePlayer = MediaPlayer.create(this, R.raw.begin_speaking_now)
        binding.lifecycleOwner = this
        binding.viewModel = part3ViewModel
        binding.problemToolBar.setOnCloseClickListener { finish() }
        binding.buttonAudioController.setOnStateClickListener(this)
        binding.buttonRestart.setOnClickListener { cancelRecord() }
        binding.buttonSkip.setOnClickListener { skipPreparation() }
        binding.buttonNext.setOnClickListener { startNextProblem() }
        part3ViewModel.toastMessage.observe(this) { showToast(it) }
    }

    override fun onInitialPermissionGranted() {
        part3ViewModel.loadProblem(getProblemNumber())
    }

    override fun onAudioButtonClick(state: AudioStateButton.State) = when (state) {
        AudioStateButton.State.RECORDING -> startRecord(RESPONSE_TIME_15)
        AudioStateButton.State.STOP -> cancelRecord()
        AudioStateButton.State.PLAYING -> part3ViewModel.playRecord()
        AudioStateButton.State.PAUSE -> part3ViewModel.pausePlayRecord()
    }

    private fun getProblemNumber(): Int =
        intent.getIntExtra(ProblemGuideActivity.KEY_PROBLEM_NUMBER, 1)

    private fun skipPreparation() {
        part3ViewModel.skipProgress()
        prepareNoticePlayer?.pause()
        if (beepPlayer?.isPlaying == true) {
            beepPlayer?.pause()
            beepPlayer?.seekTo(0)
        }
    }

    private fun startProblem() {
        problemPlayer = MediaPlayer().apply {
            setDataSource(part3ViewModel.getProblemSoundUrl())
            prepare()
            setOnCompletionListener { it.reset(); startSubProblem() }
        }
        problemPlayer?.start()
    }

    private fun startSubProblem() {
        if (part3ViewModel.isFinishSubProblems()) return
        problemPlayer?.let {
            it.setDataSource(part3ViewModel.getSubProblemSoundUrl())
            it.prepare()
            it.setOnCompletionListener { end -> end.reset(); startPreparation() }
        }
        requireBinding().zoneSubProblem.visibility = View.VISIBLE
        problemPlayer?.start()
    }

    private fun startPreparation() {
        binding?.zoneTimer?.visibility = View.VISIBLE
        rewindProgressBar(PREPARATION_TIME)
        playSound(prepareNoticePlayer) {
            playSound(beepPlayer) { part3ViewModel.startCountDown(PREPARATION_TIME) }
        }
        part3ViewModel.setOnProgressFinishListener { startSpeakingTime() }
    }

    private fun startSpeakingTime() {
        part3ViewModel.changeState(ProblemState.RESPONSE)
        rewindProgressBar(RESPONSE_TIME_15)
        playSound(speakingNoticePlayer) {
            playSound(beepPlayer) { startRecord(RESPONSE_TIME_15) }
        }
    }

    private fun rewindProgressBar(maxProgress: Int) {
        requireBinding().progressBar.maxProgress = maxProgress
        part3ViewModel.resetProgress()
    }

    private fun startRecord(duration: Int) {
        part3ViewModel.prepareRecorder(getExternalDirectoryPath())
        part3ViewModel.startRecord(duration)
        part3ViewModel.setOnProgressFinishListener {
            part3ViewModel.saveRecord()
            playSound(beepPlayer)
            if (part3ViewModel.isFinishSubProblems()) deployStopTalkingButtonsDialog()
            else deployStopTalkingPauseDialog()
        }
    }

    private fun deployStopTalkingButtonsDialog() {
        StopTalkingButtonsDialog(this).apply {
            setOnCheckProblemClickListener { listenMyRecord() }
            setOnNextClickListener { startNextProblem() }
        }.show()
//        part3ViewModel.saveSolvedProblem()
        //FIXME 나중에 푼 문제 저장되는것 주석 해제 할 것.
    }

    private fun deployStopTalkingPauseDialog() {
        StopTalkingPauseDialog(this).apply {
            setOnFinishListener { startSubProblem() }
        }.show()
    }

    private fun listenMyRecord() {
        part3ViewModel.changeState(ProblemState.MY_RECORD)
        requireBinding().progressBar.initToProgressBar(part3ViewModel.getRecordDuration())
        requireBinding().progressBar.setOnProgressChangeListener { part3ViewModel.playRecord(it) }
        part3ViewModel.playRecord(0)
    }

    private fun cancelRecord() {
        part3ViewModel.cancelRecord()
        speakingNoticePlayer?.pause()
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
        speakingNoticePlayer?.release()
    }

    companion object {
        private val PREPARATION_TIME = TimeUnit.SECONDS.toMillis(3).toInt()
        private val RESPONSE_TIME_15 = TimeUnit.SECONDS.toMillis(4).toInt()
        private val RESPONSE_TIME_30 = TimeUnit.SECONDS.toMillis(5).toInt()
    }
}
