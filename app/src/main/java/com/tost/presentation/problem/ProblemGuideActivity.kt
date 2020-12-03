package com.tost.presentation.problem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tost.data.entity.Part
import com.tost.databinding.ActivityProblemGuideBinding

// TODO: 들어온 파트에 따라 음성파일 다르게 받아서 재생시키기
// TODO: 들어온 문제번호와 파트에 따라 해당 파트 액티비티 띄우기
class ProblemGuideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityProblemGuideBinding.inflate(layoutInflater)
        binding.part = getPart()
        setContentView(binding.root)
    }

    private fun getPart(): Part = intent.getSerializableExtra(KEY_PART) as? Part
        ?: throw IllegalArgumentException("part must be send")

    private fun getProblemNumber(): Int = intent.getIntExtra(KEY_PART, -1)

    companion object {
        const val KEY_PART = "part"
        const val KEY_PROBLEM_NUMBER = "problemNumber"
    }
}