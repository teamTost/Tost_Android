package com.tost.presentation.goal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tost.data.repository.GoogleAuthRepository
import com.tost.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class GoalViewModel @ViewModelInject constructor(
    private val googleAuthRepository: GoogleAuthRepository
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date>
        get() = _selectedDate

    private val _selectedLevel = MutableLiveData<Int>()
    val selectedLevel: LiveData<Int>
        get() = _selectedLevel

    init {
        initViewModel()
    }

    private fun initViewModel() = viewModelScope.launch {
        _userName.value = googleAuthRepository.getName()
    }

    fun refreshSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    fun refreshSelectedLevel(level: Int) {
        _selectedLevel.value = level
    }
}
