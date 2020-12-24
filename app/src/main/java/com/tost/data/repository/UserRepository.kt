package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.tost.data.dao.get
import com.tost.data.service.TostService
import com.tost.data.service.request.TostLoginRequestParams
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class UserRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tostService: TostService,
) {
    suspend fun getTostToken(): String? {
        return dataStore[KEY_TOST_TOKEN].first()
    }

    suspend fun saveTostToken(googleToken: String, fcmToken: String) {
        val remoteTostToken = getRemoteTostToken(googleToken, fcmToken)
        dataStore.edit { it[KEY_TOST_TOKEN] = remoteTostToken }
    }

    private suspend fun getRemoteTostToken(googleToken: String, fcmToken: String): String {
        val params = TostLoginRequestParams(googleToken, fcmToken)
        val response = tostService.login(params)
        return response.token.also { printLog("remote Tost Token saved $it") }
    }

    companion object {
        private val KEY_TOST_TOKEN = preferencesKey<String>("KEY_TOST_TOKEN")
    }
}
