package com.tost.presentation.goal.weekly.change

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.R
import com.tost.databinding.ActivityChangeWeeklyGoalBinding
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeWeeklyGoalActivity : AppCompatActivity() {

    private val changeWeeklyGoalViewModel: ChangeWeeklyGoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityChangeWeeklyGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView(binding)
        subscribeIsSuccess()

        changeWeeklyGoalViewModel.loadExistWeeklyGoal()
    }

    private fun initView(binding: ActivityChangeWeeklyGoalBinding) {
        binding.viewModel = changeWeeklyGoalViewModel
        binding.lifecycleOwner = this
        binding.buttonModify.setOnClickListener { modifyWeeklyGoals() }
    }

    private fun modifyWeeklyGoals() {
        if (changeWeeklyGoalViewModel.isAllGoalsInserted()) changeWeeklyGoalViewModel.modifyWeeklyGoals()
        else showToast(R.string.select_goals)
    }

    private fun subscribeIsSuccess() {
        changeWeeklyGoalViewModel.isSuccess.observe(this) { isSuccess ->
            if (!isSuccess) return@observe
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val REQUEST_CODE = 2001
    }
}
