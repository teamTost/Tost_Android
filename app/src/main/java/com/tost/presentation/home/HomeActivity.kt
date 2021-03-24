package com.tost.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.R
import com.tost.data.entity.Part
import com.tost.databinding.ActivityHomeBinding
import com.tost.presentation.goal.EntireGoalActivity
import com.tost.presentation.goal.weekly.WeeklyGoalActivity
import com.tost.presentation.goal.weekly.change.ChangeWeeklyGoalActivity
import com.tost.presentation.mypage.MyPageActivity
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
        if (this.isTrial()) return
        homeViewModel.loadGoals(Date())

        subscribeEntireGoals()
        subscribeWeeklyGoals()

        binding.button2.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ChangeWeeklyGoalActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            homeViewModel.refreshWeeklyGoal()
            showToast(R.string.goal_changed)
        }
    }

    private fun isTrial(): Boolean = intent.getBooleanExtra(KEY_TRIAL, false)

    private fun initView(binding: ActivityHomeBinding) {
        val adapter = HomePracticesAdapter(Part.values())
        adapter.setOnPracticeClickListener { deployProblemEntryActivity(it) }

        binding.listPractices.adapter = adapter
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        binding.seekBar.setOnTouchListener { _, _ -> true }
        binding.buttonChangeGoal.setOnClickListener { deployChangeWeeklyGoalActivity() }
        binding.buttonGetLogin.setOnClickListener { finish() }
        binding.buttonReview.setOnClickListener { deployActivityWhenLogin(TestActivity::class.java) }
        binding.buttonTestStart.setOnClickListener { deployActivityWhenLogin(TestActivity::class.java) }
    }

    private fun subscribeEntireGoals() {
        homeViewModel.entireGoal.observe(this) {
            if (it == null) {
                deployActivityOf(EntireGoalActivity::class.java)
                finish()
            }
        }
    }

    private fun subscribeWeeklyGoals() {
        homeViewModel.weeklyGoal.observe(this) {
            if (homeViewModel.isEntireGoalNotNull() && it == null) {
                deployActivityOf(WeeklyGoalActivity::class.java)
                finish()
            }
        }
    }

    private fun <T> deployActivityOf(targetActivity: Class<T>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
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

    private fun <T> deployActivityWhenLogin(targetActivity: Class<T>) {
        if (homeViewModel.isWeeklyGoalEmpty()) {
            showToast(R.string.login_required)
            return
        }
        deployActivityOf(targetActivity)
    }

    companion object {
        const val KEY_TRIAL = "KEY_TRIAL"
    }
}
