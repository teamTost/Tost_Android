package com.tost.presentation.problem.part2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tost.data.entity.Part
import com.tost.databinding.ActivityPart2Binding

class Part2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPart2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView(binding: ActivityPart2Binding) {
        binding.part = Part.TWO
    }

    private fun startProblem() {

    }
}