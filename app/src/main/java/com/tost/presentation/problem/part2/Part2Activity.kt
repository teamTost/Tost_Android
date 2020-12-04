package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding
import com.tost.presentation.problem.base.AudioBaseActivity
import com.tost.presentation.problem.widget.TostProgressBar

class Part2Activity : AudioBaseActivity() {

    private var prepareNoticePlayer: MediaPlayer? = null
    private var readingNoticePlayer: MediaPlayer? = null
    private var beepPlayer: MediaPlayer? = null

    private var binding: ActivityPart2Binding? = null

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
    }

    private fun initView(binding: ActivityPart2Binding) {
        binding.lifecycleOwner = this
        binding.part = Part.TWO
        binding.imageUrl = ""
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
        beepPlayer?.setOnCompletionListener {
            Toast.makeText(this, "STOP TALKING", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prepareNoticePlayer?.release()
        readingNoticePlayer?.release()
        beepPlayer?.release()
        binding = null
    }
}
