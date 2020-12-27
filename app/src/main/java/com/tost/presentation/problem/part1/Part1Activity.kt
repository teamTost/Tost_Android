package com.tost.presentation.problem.part1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.tost.databinding.ActivityPart1Binding
import com.tost.presentation.problem.ProblemGuideActivity.Companion.KEY_PROBLEM_NUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Part1Activity : AppCompatActivity() {

    private val part1ViewModel: Part1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initView(binding)

        part1ViewModel.loadProblem(getProblemNumber())
    }

    private fun initView(binding: ActivityPart1Binding) {
        binding.viewModel = part1ViewModel
        binding.lifecycleOwner = this
    }

    private fun getProblemNumber(): Int = intent.getIntExtra(KEY_PROBLEM_NUMBER, 1)
}
