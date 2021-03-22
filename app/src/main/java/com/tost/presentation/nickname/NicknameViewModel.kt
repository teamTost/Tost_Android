package com.tost.presentation.nickname

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tost.R
import com.tost.data.repository.GoogleAuthRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.utils.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 3월 21, 2021
 */

class NicknameViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val googleAuthRepository: GoogleAuthRepository,
) : BaseViewModel() {

    val nickname = MutableLiveData("")
    private lateinit var savedNickname: String

    init {
        initViewModel()
    }

    private fun initViewModel() = viewModelScope.launch {
        nickname.value = googleAuthRepository.getName()
            .also { savedNickname = it }
    }

    fun saveNickname() = viewModelScope.launch {
        _isLoading.value = true
        val currentNickname = nickname.value
        if (currentNickname.isNullOrBlank()) {
            _toastMessage.value = R.string.not_blank
            _isLoading.value = false
            return@launch
        }
        if (currentNickname.length > 5) {
            _toastMessage.value = R.string.input_five_letters
            _isLoading.value = false
            return@launch
        }
        if (currentNickname == savedNickname) {
            _isSuccess.value = true
            return@launch
        }
        userRepository.saveNickname(currentNickname)
        _isSuccess.value = true
        _isLoading.value = false
    }
}
