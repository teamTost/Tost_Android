package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.data.entity.User
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
    private val userDataStore: DataStore<Preferences>,
    private val tostService: TostService,
) {
    suspend fun isNotEmpty(): Boolean {
        return userDataStore.data.map { it[KEY_TOST_TOKEN] }.first() == null
    }

    suspend fun saveUser(account: GoogleSignInAccount) {
        userDataStore.edit {
            it[KEY_EMAIL] = account.email.orEmpty()
            it[KEY_NAME] = account.displayName.orEmpty()
            it[KEY_PROFILE_URL] = account.photoUrl.toString()
            it[KEY_GOOGLE_TOKEN] = account.idToken.orEmpty()
            it[KEY_TOST_TOKEN] = getTostToken(account.idToken.orEmpty())
        }
    }

    private suspend fun getTostToken(googleToken: String): String {
        val params = TostLoginRequestParams(googleToken)
        val response = tostService.login(params)
        return response.token.also { printLog("remote Tost Token saved $it") }
    }

    suspend fun getUser(): User = User(
        email = getEmail(),
        name = getName(),
        profileUrl = getProfileUrl(),
        googleToken = getGoogleToken(),
        tostToken = getTostToken(),
    )

    suspend fun getEmail(): String {
        return userDataStore.data.map { it[KEY_EMAIL] }.first()
            ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun getName(): String {
        return userDataStore.data.map { it[KEY_NAME] }.first()
            ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun getProfileUrl(): String {
        return userDataStore.data.map { it[KEY_PROFILE_URL] }.first()
            ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun getGoogleToken(): String {
        return userDataStore.data.map { it[KEY_GOOGLE_TOKEN] }.first()
            ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun getTostToken(): String {
        return userDataStore.data.map { it[KEY_TOST_TOKEN] }.first()
            ?: getTostToken(getGoogleToken())
                .also { saveTostToken(it) }
    }

    private suspend fun saveTostToken(tostToken: String) {
        userDataStore.edit { it[KEY_TOST_TOKEN] = tostToken }
    }

    suspend fun getFcmToken(): String {
        return getLocalFcmToken()
            ?: getRemoteFcmToken().also { saveFcmToken(it) }
    }

    private suspend fun getLocalFcmToken(): String? {
        return userDataStore.data.map { it[KEY_FCM_TOKEN] }.first()
    }

    private suspend fun getRemoteFcmToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }

    private suspend fun saveFcmToken(fcmToken: String) {
        userDataStore.edit { it[KEY_FCM_TOKEN] = fcmToken }
    }

    companion object {
        private val KEY_EMAIL = preferencesKey<String>("KEY_EMAIL")
        private val KEY_NAME = preferencesKey<String>("KEY_NAME")
        private val KEY_PROFILE_URL = preferencesKey<String>("KEY_PROFILE_URL")
        private val KEY_GOOGLE_TOKEN = preferencesKey<String>("KEY_GOOGLE_TOKEN")
        private val KEY_TOST_TOKEN = preferencesKey<String>("KEY_TOST_TOKEN")
        private val KEY_FCM_TOKEN = preferencesKey<String>("KEY_FCM_TOKEN")

        private val USER_NOT_SAVED_EXCEPTION = IllegalStateException("user not saved")
    }
}
