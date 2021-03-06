package com.tost.presentation.mypage

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tost.data.repository.GoalRepository
import com.tost.data.repository.GoogleAuthRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.utils.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By Malibin
 * on 3월 19, 2021
 */

class MyPageViewModel @ViewModelInject constructor(
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

    private val _isDataCleared = MutableLiveData<Boolean>()
    val isDataCleared: LiveData<Boolean> = _isDataCleared

    var isUserQuit = false
        private set

    fun loadUserInfo() = viewModelScope.launch {
        _userName.value = googleAuthRepository.getName()
        val token = userRepository.getTostToken() ?: throw IllegalStateException("로그인 이상")
        val entireGoal = goalRepository.getEntireGoal(token)
        _selectedDate.value = entireGoal?.endDate
        _selectedLevel.value = entireGoal?.level
    }

    fun logout() = viewModelScope.launch {
        _isLoading.value = true
        userRepository.deleteAll()
        googleAuthRepository.deleteAll()
        goalRepository.deleteAll()
        _isLoading.value = false
        _isDataCleared.value = true
    }

    fun quitMember() = viewModelScope.launch {
        _isLoading.value = true
        userRepository.quitMember()
        googleAuthRepository.deleteAll()
        goalRepository.deleteAll()
        isUserQuit = true
        _isLoading.value = false
        _isDataCleared.value = true
    }
}
