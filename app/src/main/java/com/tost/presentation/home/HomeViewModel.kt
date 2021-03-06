package com.tost.presentation.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.EntireGoal
import com.tost.data.entity.WeeklyGoal
import com.tost.data.repository.GoalRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By Malibin
 * on 12월 26, 2020
 */

class HomeViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
) : ViewModel() {

    private val _entireGoal = MutableLiveData<EntireGoal>()
    val entireGoal: LiveData<EntireGoal> = _entireGoal

    private val _weeklyGoal = MutableLiveData<WeeklyGoal>()
    val weeklyGoal: LiveData<WeeklyGoal> = _weeklyGoal

    private val _goalProgress = MutableLiveData<Int>()
    val goalProgress: LiveData<Int> = _goalProgress

    fun loadGoals(now: Date) = viewModelScope.launch {
        val tostToken = userRepository.getTostToken()
            ?: throw IllegalStateException("login requested")
        val entireGoal = goalRepository.getEntireGoal(tostToken)
        _goalProgress.value = entireGoal?.getNowGoalProgress(now)
        _entireGoal.value = entireGoal
        _weeklyGoal.value = goalRepository.getWeeklyGoal(tostToken)
    }// FIXME  이렇게 짜니까 goal remote 를 두 번 찌름

    fun refreshWeeklyGoal() = viewModelScope.launch {
        val tostToken = userRepository.getTostToken()
            ?: throw IllegalStateException("login requested")
        _weeklyGoal.value = goalRepository.getWeeklyGoal(tostToken)
    }

    fun isEntireGoalNotNull(): Boolean = entireGoal.value != null

    fun isWeeklyGoalEmpty(): Boolean = weeklyGoal.value == null
}
