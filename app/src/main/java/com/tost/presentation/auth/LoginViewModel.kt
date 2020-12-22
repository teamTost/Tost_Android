package com.tost.presentation.auth

import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.tost.R
import com.tost.data.repository.GoogleAuthRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.auth.LoginActivity.Companion.REQUEST_CODE_GOOGLE_AUTH

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val googleAuthRepository: GoogleAuthRepository,
) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_CODE_GOOGLE_AUTH) {
            val googleAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).result
            if (googleAccount == null) {
                _toastMessage.value = R.string.google_login_fail
                return
            }
//            saveUser(googleAccount)
        }
    }

//    private fun saveUser(account: GoogleSignInAccount) = viewModelScope.launch {
//        userRepository.saveUser(account)
//        _isLoading.value = false
//        _loginSuccess.value = true
//    }
}
