package com.tost.presentation.auth

import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.tost.R
import com.tost.data.entity.User
import com.tost.data.service.TostService
import com.tost.presentation.auth.LoginActivity.Companion.REQUEST_CODE_GOOGLE_AUTH

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class LoginViewModel @ViewModelInject constructor(
    private val tostService: TostService
) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int>
        get() = _toastMessage

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == REQUEST_CODE_GOOGLE_AUTH) {
            val googleAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).result
            if (googleAccount == null) {
                _toastMessage.value = R.string.google_login_fail
                return
            }
            val googleToken = googleAccount.idToken
//            val user = User.from(googleAccount)
        }
    }
}