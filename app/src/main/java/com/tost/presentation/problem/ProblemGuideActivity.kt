package com.tost.presentation.problem

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tost.data.entity.Part
import com.tost.databinding.ActivityProblemGuideBinding
import com.tost.presentation.problem.part1.Part1Activity
import com.tost.presentation.problem.part2.Part2Activity
import com.tost.presentation.problem.part5.Part5Activity
import com.tost.presentation.problem.part6.Part6Activity
import com.tost.presentation.utils.showTostToast

class ProblemGuideActivity : AppCompatActivity() {

    private var guideAudioPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProblemGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
        showTostToast("볼륨을 높여주세요!")
    }

    private fun initView(binding: ActivityProblemGuideBinding) {
        val part = getPart()
        binding.part = part
        binding.buttonClose.setOnClickListener { finishActivity() }
        binding.buttonSkip.setOnClickListener { deployProblemPartActivity() }
        guideAudioPlayer = MediaPlayer.create(this, part.guideAudio)
        guideAudioPlayer?.setOnCompletionListener { deployProblemPartActivity() }
        guideAudioPlayer?.start()
    }

    private fun getPart(): Part = intent.getSerializableExtra(KEY_PART) as? Part
        ?: throw IllegalArgumentException("part must be send")

    private fun getProblemNumber(): Int = intent.getIntExtra(KEY_PART, 1)

    private fun finishActivity() {
        guideAudioPlayer?.stop()
        finish()
    }

    private fun deployProblemPartActivity() {
        guideAudioPlayer?.stop()
        val intent = Intent(this, getPartActivityClass(getPart()))
        intent.putExtra(KEY_PROBLEM_NUMBER, getProblemNumber())
        startActivity(intent)
        finish()
    }

    private fun getPartActivityClass(part: Part): Class<*> = when (part) {
        Part.ONE -> Part1Activity::class.java
        Part.TWO -> Part2Activity::class.java
        Part.FIVE -> Part5Activity::class.java
        Part.SIX -> Part6Activity::class.java
        else -> Part1Activity::class.java
    }

    override fun onDestroy() {
        super.onDestroy()
        guideAudioPlayer?.stop()
        guideAudioPlayer?.release()
        guideAudioPlayer = null
    }

    companion object {
        const val KEY_PART = "part"
        const val KEY_PROBLEM_NUMBER = "problemNumber"
    }
}
