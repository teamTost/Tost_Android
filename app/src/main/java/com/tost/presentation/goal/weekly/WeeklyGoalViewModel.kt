package com.tost.presentation.goal.weekly

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.WeeklyGoal
import com.tost.data.repository.GoalRepository
import com.tost.data.repository.UserRepository
import com.tost.data.service.TostService
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 24, 2020
 */

class WeeklyGoalViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val tostService: TostService,
) : ViewModel() {
    val part1Goal = MutableLiveData<String>()
    val part2Goal = MutableLiveData<String>()
    val part3Goal = MutableLiveData<String>()
    val part4Goal = MutableLiveData<String>()
    val part5Goal = MutableLiveData<String>()
    val part6Goal = MutableLiveData<String>()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun isAllGoalsInserted(): Boolean {
        return !part1Goal.value.isNullOrBlank()
                && !part2Goal.value.isNullOrBlank()
                && !part3Goal.value.isNullOrBlank()
                && !part4Goal.value.isNullOrBlank()
                && !part5Goal.value.isNullOrBlank()
                && !part6Goal.value.isNullOrBlank()
    }

    fun saveWeeklyGoals() = viewModelScope.launch {
        _isLoading.value = true
        val weeklyGoal = getWeeklyGoal()
        val tostToken = userRepository.getTostToken()
            ?: throw IllegalStateException("login requested")
        tostService.saveWeeklyGoal(tostToken, getWeeklyGoal().toParams())
        goalRepository.saveWeeklyGoal(weeklyGoal)
        _isLoading.value = false
    }

    private fun getWeeklyGoal(): WeeklyGoal {
        return WeeklyGoal(
            part1 = part1Goal.value?.toInt() ?: throw INPUT_NULL_EXCEPTION,
            part2 = part2Goal.value?.toInt() ?: throw INPUT_NULL_EXCEPTION,
            part3 = part3Goal.value?.toInt() ?: throw INPUT_NULL_EXCEPTION,
            part4 = part4Goal.value?.toInt() ?: throw INPUT_NULL_EXCEPTION,
            part5 = part5Goal.value?.toInt() ?: throw INPUT_NULL_EXCEPTION,
            part6 = part6Goal.value?.toInt() ?: throw INPUT_NULL_EXCEPTION,
        )
    }

    companion object {
        private val INPUT_NULL_EXCEPTION = IllegalStateException("goal value cannot be null")
    }
}
