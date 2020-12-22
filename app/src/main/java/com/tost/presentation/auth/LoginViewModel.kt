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
import com.tost.data.repository.UserRepository
import com.tost.presentation.auth.LoginActivity.Companion.REQUEST_CODE_GOOGLE_AUTH
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class LoginViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

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
        userRepository.saveAccount(account)
        runAutoLogin(account.idToken)
    }

    @JvmOverloads
    fun runAutoLogin(_googleToken: String? = null) = viewModelScope.launch(loginFailHandler()) {
        val googleToken = _googleToken ?: userRepository.getGoogleToken()
        require(googleToken.isNotBlank()) { "google Token not saved" }
        val tostToken = userRepository.getTostToken()
        require(tostToken.isNotBlank()) { "Tost Token not saved" }
        loginSuccess()
    }

    private fun loginSuccess() {
        _isLoading.value = false
        _loginSuccess.value = true
    }

    private fun loginFailHandler() = CoroutineExceptionHandler { _, t ->
        _isLoading.value = false
        t.printStackTrace()
    }
}
