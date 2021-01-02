package com.tost.presentation.utils

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created By Malibin
 * on 12월 27, 2020
 */

open class BaseViewModel : ViewModel() {
    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    protected val _isSuccess = MutableLiveData(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    protected val _toastMessage = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> = _toastMessage

    protected fun changeLoadingTo(flag: Boolean) {
        _isLoading.value = flag
    }

    protected fun sendToastMessage(@StringRes resId: Int) {
        _toastMessage.value = resId
    }
}
