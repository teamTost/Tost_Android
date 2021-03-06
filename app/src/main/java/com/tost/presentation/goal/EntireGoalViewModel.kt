package com.tost.presentation.goal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tost.data.entity.EntireGoal
import com.tost.data.repository.GoalRepository
import com.tost.data.repository.GoogleAuthRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.utils.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class EntireGoalViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val googleAuthRepository: GoogleAuthRepository,
    private val goalRepository: GoalRepository,
) : BaseViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> = _selectedDate

    private val _selectedLevel = MutableLiveData<Int>()
    val selectedLevel: LiveData<Int> = _selectedLevel

    init {
        initViewModel()
    }

    private fun initViewModel() = viewModelScope.launch {
        _userName.value = googleAuthRepository.getName()
        val token = userRepository.getTostToken() ?: throw IllegalStateException("로그인 이상")
        val entireGoal = goalRepository.getEntireGoal(token)
        _selectedDate.value = entireGoal?.endDate
        _selectedLevel.value = entireGoal?.level
    }

    fun refreshSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    fun refreshSelectedLevel(level: Int) {
        _selectedLevel.value = level
    }

    fun isBothGoalSelected(): Boolean {
        return selectedDate.value != null && selectedLevel.value != null
    }

    fun saveGoal() = viewModelScope.launch(goalSaveFailHandler()) {
        _isLoading.value = true
        val tostToken = userRepository.getTostToken() ?: throw IllegalStateException("로그인 이상")
        goalRepository.saveEntireGoal(tostToken, getEntireGoal())
        _isLoading.value = false
        _isSuccess.value = true
    }

    private fun getEntireGoal(): EntireGoal = EntireGoal(
        level = _selectedLevel.value ?: throw IllegalStateException("level must be selected"),
        startDate = Date(),
        endDate = _selectedDate.value ?: throw IllegalStateException("date must be selected"),
    )

    private fun goalSaveFailHandler() = CoroutineExceptionHandler { _, t ->
        _isLoading.value = false
        t.printStackTrace()
    }
}
//TODO 양방향데이터바인딩 으로 리팩터링 가능하려나?
