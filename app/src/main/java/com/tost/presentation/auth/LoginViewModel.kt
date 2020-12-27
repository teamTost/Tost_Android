package com.tost.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.tost.R
import com.tost.data.repository.GoogleAuthRepository
import com.tost.data.repository.UserRepository
import com.tost.presentation.auth.LoginActivity.Companion.REQUEST_CODE_GOOGLE_AUTH
import com.tost.presentation.utils.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val googleAuthRepository: GoogleAuthRepository,
) : BaseViewModel() {

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != REQUEST_CODE_GOOGLE_AUTH) return
        if (resultCode != Activity.RESULT_OK) {
            _isLoading.value = false
            return
        }
        val googleAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).result
        if (googleAccount == null) {
            _toastMessage.value = R.string.google_login_fail
            return
        }
        saveUser(googleAccount)
    }

    private fun saveUser(account: GoogleSignInAccount) = viewModelScope.launch(loginFailHandler()) {
        googleAuthRepository.saveAccount(account)
        userRepository.saveTostToken(
            googleAuthRepository.getGoogleToken(),
            googleAuthRepository.getFcmToken(),
        )
        runAutoLogin()
    }

    fun runAutoLogin() = viewModelScope.launch(loginFailHandler()) {
        userRepository.getTostToken()
            ?: throw IllegalStateException("Tost Token not exist in Local")
        loginSuccess()
    }

    private fun loginSuccess() {
        _isLoading.value = false
        _isSuccess.value = true
    }

    private fun loginFailHandler() = CoroutineExceptionHandler { _, t ->
        _isLoading.value = false
        t.printStackTrace()
    }
}
