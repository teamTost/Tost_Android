package com.tost.presentation.goal

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tost.databinding.ActivityGoalBinding
import com.tost.presentation.goal.dialog.DatePickBottomSheet
import com.tost.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class GoalActivity : AppCompatActivity(), DatePickBottomSheet.OnDatePickListener {

    private val goalViewModel: GoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
    }

    private fun initView(binding: ActivityGoalBinding) {
        binding.lifecycleOwner = this
        binding.viewModel = goalViewModel
        binding.buttonGoalDate.setOnClickListener { deployDatePicker() }
        binding.buttonGoalLevel.setOnClickListener { }
    }

    private fun deployDatePicker() {
        val bottomSheet = supportFragmentManager
            .findFragmentByTag(TAG_DATE_BOTTOM_SHEET) as? DatePickBottomSheet
            ?: DatePickBottomSheet()
        bottomSheet.setOnDatePickListener(this)
        bottomSheet.show(supportFragmentManager, TAG_DATE_BOTTOM_SHEET)
    }

    override fun onDatePick(date: Date) {
        if (date.time < System.currentTimeMillis()) {
            showToast("과거는 설정할 수 없어요")
            return
        }
        goalViewModel.refreshSelectedDate(date)
    }

//    private fun deployLevelPicker() {
//
//    }

    companion object {
        private const val TAG_DATE_BOTTOM_SHEET = "date_bottom_sheet"
    }
}
