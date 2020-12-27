package com.tost.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityHomeBinding
import com.tost.presentation.goal.weekly.change.ChangeWeeklyGoalActivity
import com.tost.presentation.problem.ProblemEntryActivity
import com.tost.presentation.test.TestActivity
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView(binding)
        homeViewModel.loadGoals(Date())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ChangeWeeklyGoalActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                homeViewModel.refreshWeeklyGoal()
                showToast(R.string.goal_changed)
            }
        }
    }

    private fun initView(binding: ActivityHomeBinding) {
        val adapter = HomePracticesAdapter(Part.values())
        adapter.setOnPracticeClickListener { deployProblemEntryActivity(it) }

        binding.listPractices.adapter = adapter
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        binding.seekBar.setOnTouchListener { _, _ -> true }
        binding.buttonChangeGoal.setOnClickListener { deployChangeWeeklyGoalActivity() }
        binding.buttonTestStart.setOnClickListener { deployTestActivity() }
    }

    private fun deployProblemEntryActivity(part: Part) {
        val intent = Intent(this, ProblemEntryActivity::class.java)
        intent.putExtra(ProblemEntryActivity.KEY_PART, part)
        startActivity(intent)
    }

    private fun deployChangeWeeklyGoalActivity() {
        val intent = Intent(this, ChangeWeeklyGoalActivity::class.java)
        startActivityForResult(intent, ChangeWeeklyGoalActivity.REQUEST_CODE)
    }

    private fun deployTestActivity() {
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
    }
}
