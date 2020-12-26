package com.tost.presentation.goal.weekly.change

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.WeeklyGoal
import com.tost.data.repository.GoalRepository
import com.tost.data.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 26, 2020
 */

class ChangeWeeklyGoalViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
) : ViewModel() {

    val part1Goal = MutableLiveData<String>()
    val part2Goal = MutableLiveData<String>()
    val part3Goal = MutableLiveData<String>()
    val part4Goal = MutableLiveData<String>()
    val part5Goal = MutableLiveData<String>()
    val part6Goal = MutableLiveData<String>()

    private val _weeklyGoal = MutableLiveData<WeeklyGoal>()
    val weeklyGoal: LiveData<WeeklyGoal>
        get() = _weeklyGoal

    private val _isSuccess = MutableLiveData(false)
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadExistWeeklyGoal() = viewModelScope.launch {
        val tostToken = userRepository.getTostToken()
            ?: throw IllegalStateException("login requested")
        val weeklyGoal = goalRepository.getWeeklyGoal(tostToken)
        _weeklyGoal.value = weeklyGoal
        part1Goal.value = weeklyGoal?.part1.toString()
        part2Goal.value = weeklyGoal?.part2.toString()
        part3Goal.value = weeklyGoal?.part3.toString()
        part4Goal.value = weeklyGoal?.part4.toString()
        part5Goal.value = weeklyGoal?.part5.toString()
        part6Goal.value = weeklyGoal?.part6.toString()
    }

    fun isAllGoalsInserted(): Boolean {
        return !part1Goal.value.isNullOrBlank()
                && !part2Goal.value.isNullOrBlank()
                && !part3Goal.value.isNullOrBlank()
                && !part4Goal.value.isNullOrBlank()
                && !part5Goal.value.isNullOrBlank()
                && !part6Goal.value.isNullOrBlank()
    }

    fun modifyWeeklyGoals() = viewModelScope.launch {
        _isLoading.value = true
        if(isGoalChanged()){
            val tostToken = userRepository.getTostToken()
                ?: throw IllegalStateException("login requested")
            goalRepository.saveWeeklyGoal(tostToken, getWeeklyGoal())
        }
        _isLoading.value = false
        _isSuccess.value = true
    }

    fun isGoalChanged(): Boolean{
        if(part1Goal.value?.toInt() != weeklyGoal.value?.part1) return true
        if(part2Goal.value?.toInt() != weeklyGoal.value?.part2) return true
        if(part3Goal.value?.toInt() != weeklyGoal.value?.part3) return true
        if(part4Goal.value?.toInt() != weeklyGoal.value?.part4) return true
        if(part5Goal.value?.toInt() != weeklyGoal.value?.part5) return true
        if(part6Goal.value?.toInt() != weeklyGoal.value?.part6) return true
        return false
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
