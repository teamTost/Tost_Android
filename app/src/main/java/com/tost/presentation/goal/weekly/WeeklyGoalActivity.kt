package com.tost.presentation.goal.weekly

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.R
import com.tost.databinding.ActivityWeeklyGoalBinding
import com.tost.presentation.home.HomeActivity
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeeklyGoalActivity : AppCompatActivity() {

    private val weeklyGoalViewModel: WeeklyGoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWeeklyGoalBinding.inflate(layoutInflater)
        binding.viewModel = weeklyGoalViewModel
        setContentView(binding.root)

        initView(binding)
        subscribeSaveSuccess()
    }

    private fun initView(binding: ActivityWeeklyGoalBinding) {
        binding.buttonSubmit.setOnClickListener {
            if (weeklyGoalViewModel.isAllGoalsInserted()) weeklyGoalViewModel.saveWeeklyGoals()
            else showToast(R.string.select_goals)
        }
    }

    private fun subscribeSaveSuccess() {
        weeklyGoalViewModel.isSuccess.observe(this) { if (it == true) deployHomeActivityAndFinish() }
    }

    private fun deployHomeActivityAndFinish() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
