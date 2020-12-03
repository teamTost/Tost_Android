package com.tost.presentation.problem.part2

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding

class Part2Activity : AppCompatActivity() {

    private var prepareAudioPlayer: MediaPlayer? = null
    private var readingAudioPlayer: MediaPlayer? = null
    private var beepAudioPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initAudio()
        initView(binding)
        startProblem(binding)
    }

    private fun initView(binding: ActivityPart2Binding) {
        binding.lifecycleOwner = this
        binding.part = Part.TWO
    }

    private fun initAudio() {
        prepareAudioPlayer = MediaPlayer.create(this, R.raw.begin_preparing_now)
        readingAudioPlayer = MediaPlayer.create(this, R.raw.begin_reading_aloud_now)
        beepAudioPlayer = MediaPlayer.create(this, R.raw.beep)
    }

    private fun startProblem(binding: ActivityPart2Binding) {
        prepareAudioPlayer?.start()
        prepareAudioPlayer?.setOnCompletionListener {
            beepAudioPlayer?.setOnCompletionListener { binding.progressBar.start() }
            beepAudioPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prepareAudioPlayer?.release()
        readingAudioPlayer?.release()
        beepAudioPlayer?.release()
    }
}
