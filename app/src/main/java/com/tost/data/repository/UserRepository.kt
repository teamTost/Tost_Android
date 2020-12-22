package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.tost.data.service.TostService
import com.tost.data.service.request.TostLoginRequestParams
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class UserRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tostService: TostService,
) {
    private suspend fun getTostToken(googleToken: String): String {
        return getLocalTostToken()
            ?: getRemoteTostToken(googleToken).also { saveTostToken(it) }
    }

    private suspend fun getLocalTostToken(): String? {
        return dataStore.data.map { it[KEY_TOST_TOKEN] }.first()
    }

    private suspend fun getRemoteTostToken(googleToken: String): String {
        val params = TostLoginRequestParams(googleToken)
        val response = tostService.login(params)
        return response.token.also { printLog("remote Tost Token saved $it") }
    }

    private suspend fun saveTostToken(tostToken: String) {
        dataStore.edit { it[KEY_TOST_TOKEN] = tostToken }
    }

    companion object {
        private val KEY_TOST_TOKEN = preferencesKey<String>("KEY_TOST_TOKEN")

        private val USER_NOT_SAVED_EXCEPTION = IllegalStateException("user not saved")
    }
}
