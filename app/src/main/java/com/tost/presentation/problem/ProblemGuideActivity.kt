package com.tost.presentation.problem

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tost.data.entity.Part
import com.tost.databinding.ActivityProblemGuideBinding
import com.tost.presentation.utils.showTostToast

// TODO: 들어온 문제번호와 파트에 따라 해당 파트 액티비티 띄우기
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
        binding.buttonSkip.root.setOnClickListener { deployProblemPartActivity() }
        guideAudioPlayer = MediaPlayer.create(this, part.guideAudio)
        guideAudioPlayer?.setOnCompletionListener { deployProblemPartActivity() }
        guideAudioPlayer?.start()
    }

    private fun getPart(): Part = intent.getSerializableExtra(KEY_PART) as? Part
        ?: throw IllegalArgumentException("part must be send")

    private fun getProblemNumber(): Int = intent.getIntExtra(KEY_PART, -1)

    private fun finishActivity() {
        guideAudioPlayer?.stop()
        finish()
    }

    private fun deployProblemPartActivity() {
        guideAudioPlayer?.stop()
        val intent = Intent()
//        intent.putExtra()
//        startActivity(intent)
        Toast.makeText(this, "다음문제 시작", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        guideAudioPlayer?.release()
        guideAudioPlayer = null
    }

    companion object {
        const val KEY_PART = "part"
        const val KEY_PROBLEM_NUMBER = "problemNumber"
    }
}