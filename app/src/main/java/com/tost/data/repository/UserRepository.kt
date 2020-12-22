package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.data.service.TostService
import com.tost.data.service.request.TostLoginRequestParams
import com.tost.presentation.utils.printLog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class UserRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val firebaseMessaging: FirebaseMessaging,
    private val tostService: TostService,
) {
    suspend fun getTostToken(): String {
        return getLocalTostToken()
            ?: getRemoteTostToken(getGoogleToken(), getFcmToken()).also { saveTostToken(it) }
    }

    private suspend fun getLocalTostToken(): String? {
        return dataStore.data.map { it[KEY_TOST_TOKEN] }.first()
    }

    private suspend fun getRemoteTostToken(googleToken: String, fcmToken: String): String {
        val params = TostLoginRequestParams(googleToken, fcmToken)
        val response = tostService.login(params)
        return response.token.also { printLog("remote Tost Token saved $it") }
    }

    private suspend fun saveTostToken(tostToken: String) {
        dataStore.edit { it[KEY_TOST_TOKEN] = tostToken }
    }

    suspend fun saveAccount(account: GoogleSignInAccount) {
        dataStore.edit { editor ->
            account.displayName?.let { editor[KEY_NAME] = it }
            account.idToken?.let { editor[KEY_GOOGLE_TOKEN] = it }
        }
    }

    suspend fun getName(): String {
        return dataStore.data.map { it[KEY_NAME] }.first()
            ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun getGoogleToken(): String {
        return dataStore.data.map { it[KEY_GOOGLE_TOKEN] }.first()
            ?: throw USER_NOT_SAVED_EXCEPTION
    }

    private suspend fun getFcmToken(): String {
        return getLocalFcmToken()
            ?: getRemoteFcmToken().also { saveFcmToken(it) }
    }

    private suspend fun getLocalFcmToken(): String? {
        return dataStore.data.map { it[KEY_FCM_TOKEN] }.first()
    }

    private suspend fun getRemoteFcmToken(): String {
        return firebaseMessaging.token.await()
    }

    private suspend fun saveFcmToken(fcmToken: String) {
        dataStore.edit { it[KEY_FCM_TOKEN] = fcmToken }
    }

    companion object {
        private val KEY_TOST_TOKEN = preferencesKey<String>("KEY_TOST_TOKEN")
        private val KEY_NAME = preferencesKey<String>("KEY_NAME")
        private val KEY_GOOGLE_TOKEN = preferencesKey<String>("KEY_GOOGLE_TOKEN")
        private val KEY_FCM_TOKEN = preferencesKey<String>("KEY_FCM_TOKEN")

        private val USER_NOT_SAVED_EXCEPTION = IllegalStateException("user not saved")
    }
}
