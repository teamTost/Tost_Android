package com.tost.presentation.goal

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tost.R
import com.tost.data.entity.WeeklyGoal
import com.tost.databinding.ActivityEntireGoalBinding
import com.tost.presentation.goal.dialog.DatePickBottomSheet
import com.tost.presentation.goal.dialog.LevelPickBottomSheet
import com.tost.presentation.goal.weekly.WeeklyGoalActivity
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EntireGoalActivity : AppCompatActivity(), DatePickBottomSheet.OnDatePickListener,
    LevelPickBottomSheet.OnLevelPickListener {

    private val entireGoalViewModel: EntireGoalViewModel by viewModels()
    private var isModifyRequested: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isModifyRequested = getIsModifyRequested()
        val binding = ActivityEntireGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
        subscribeSaveSuccess()
    }

    private fun getIsModifyRequested() = intent.getBooleanExtra(KEY_IS_MODIFY, false)

    private fun initView(binding: ActivityEntireGoalBinding) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.cloudy_orange)
        binding.lifecycleOwner = this
        binding.viewModel = entireGoalViewModel
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
        entireGoalViewModel.refreshSelectedDate(date)
    }

    override fun onLevelPick(level: Int) {
        entireGoalViewModel.refreshSelectedLevel(level)
    }

    private fun saveGoal() {
        if (entireGoalViewModel.isBothGoalSelected()) {
            entireGoalViewModel.saveGoal()
            return
        }
        showToast(R.string.select_goals)
    }

    private fun subscribeSaveSuccess() {
        entireGoalViewModel.isSuccess.observe(this) { if (it == true) deployWeeklyGoalActivityAndFinish() }
    }

    private fun deployWeeklyGoalActivityAndFinish() {
        if (isModifyRequested) {
            finish()
            return
        }
        val intent = Intent(this, WeeklyGoalActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG_DATE_BOTTOM_SHEET = "TAG_DATE_BOTTOM_SHEET"
        private const val TAG_LEVEL_BOTTOM_SHEET = "TAG_LEVEL_BOTTOM_SHEET"
        const val KEY_IS_MODIFY = "KEY_IS_MODIFY"
    }
}
