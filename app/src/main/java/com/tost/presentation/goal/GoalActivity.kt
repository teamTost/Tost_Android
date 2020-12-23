package com.tost.presentation.goal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tost.R
import com.tost.databinding.ActivityGoalBinding
import com.tost.presentation.goal.dialog.DatePickBottomSheet
import com.tost.presentation.goal.dialog.LevelPickBottomSheet
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class GoalActivity : AppCompatActivity(), DatePickBottomSheet.OnDatePickListener,
    LevelPickBottomSheet.OnLevelPickListener {

    private val goalViewModel: GoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
    }

    private fun initView(binding: ActivityGoalBinding) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.cloudy_orange)
        binding.lifecycleOwner = this
        binding.viewModel = goalViewModel
        binding.buttonGoalDate.setOnClickListener { deployDatePicker() }
        binding.buttonGoalLevel.setOnClickListener { deployLevelPicker() }
        binding.buttonStart.setOnClickListener { saveGoal() }
    }

    private fun deployDatePicker() {
        val bottomSheet = supportFragmentManager
            .findFragmentByTag(TAG_DATE_BOTTOM_SHEET) as? DatePickBottomSheet
            ?: DatePickBottomSheet()
        bottomSheet.setOnDatePickListener(this)
        bottomSheet.show(supportFragmentManager, TAG_DATE_BOTTOM_SHEET)
    }

    private fun deployLevelPicker() {
        val bottomSheet = supportFragmentManager
            .findFragmentByTag(TAG_LEVEL_BOTTOM_SHEET) as? LevelPickBottomSheet
            ?: LevelPickBottomSheet()
        bottomSheet.setOnLevelPickListener(this)
        bottomSheet.show(supportFragmentManager, TAG_LEVEL_BOTTOM_SHEET)
    }

    override fun onDatePick(date: Date) {
        if (date.time < System.currentTimeMillis()) {
            showToast("과거는 설정할 수 없어요")
            return
        }
        goalViewModel.refreshSelectedDate(date)
    }

    override fun onLevelPick(level: Int) {
        goalViewModel.refreshSelectedLevel(level)
    }

    private fun saveGoal() {
        if (goalViewModel.isBothGoalSelected()) {
            goalViewModel.saveGoal()
            return
        }
        showToast(R.string.select_goals)
    }

    companion object {
        private const val TAG_DATE_BOTTOM_SHEET = "date_bottom_sheet"
        private const val TAG_LEVEL_BOTTOM_SHEET = "level_bottom_sheet"
    }
}
