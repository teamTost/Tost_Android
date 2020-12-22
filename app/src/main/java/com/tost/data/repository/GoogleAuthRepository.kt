package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 22, 2020
 */

class GoogleAuthRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val firebaseMessaging: FirebaseMessaging,
) {
    suspend fun saveUser(account: GoogleSignInAccount) {
        dataStore.edit {
            it[KEY_NAME] = account.displayName.orEmpty()
            it[KEY_GOOGLE_TOKEN] = account.idToken.orEmpty()
        }
    }

    suspend fun getName(): String {
        return dataStore.data.map { it[KEY_NAME] }.first().orEmpty()
    }

    suspend fun getGoogleToken(): String {
        return dataStore.data.map { it[KEY_GOOGLE_TOKEN] }.first().orEmpty()
    }

    suspend fun getFcmToken(): String {
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
        private val KEY_NAME = preferencesKey<String>("KEY_NAME")
        private val KEY_GOOGLE_TOKEN = preferencesKey<String>("KEY_GOOGLE_TOKEN")

        private val KEY_FCM_TOKEN = preferencesKey<String>("KEY_FCM_TOKEN")
    }
}
