package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.remove
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.messaging.FirebaseMessaging
import com.tost.data.dao.get
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class GoogleAuthRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val firebaseMessaging: FirebaseMessaging,
) {
    suspend fun getGoogleToken(): String {
        return dataStore[KEY_GOOGLE_TOKEN].first() ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun getName(): String {
        return dataStore[KEY_NAME].first() ?: throw USER_NOT_SAVED_EXCEPTION
    }

    suspend fun saveAccount(account: GoogleSignInAccount) {
        dataStore.edit { editor ->
            account.displayName?.let { editor[KEY_NAME] = it }
            account.idToken?.let { editor[KEY_GOOGLE_TOKEN] = it }
        }
    }

    suspend fun getFcmToken(): String {
        return getLocalFcmToken()
            ?: getRemoteFcmToken().also { saveFcmToken(it) }
    }

    private suspend fun getLocalFcmToken(): String? {
        return dataStore[KEY_FCM_TOKEN].first()
    }

    private suspend fun getRemoteFcmToken(): String {
        return firebaseMessaging.token.await()
    }

    private suspend fun saveFcmToken(fcmToken: String) {
        dataStore.edit { it[KEY_FCM_TOKEN] = fcmToken }
    }

    suspend fun deleteAll() {
        dataStore.edit {
            it.remove(KEY_GOOGLE_TOKEN)
            it.remove(KEY_FCM_TOKEN)
        }
    }

    companion object {
        private val KEY_NAME = preferencesKey<String>("KEY_NAME")
        private val KEY_GOOGLE_TOKEN = preferencesKey<String>("KEY_GOOGLE_TOKEN")
        private val KEY_FCM_TOKEN = preferencesKey<String>("KEY_FCM_TOKEN")

        private val USER_NOT_SAVED_EXCEPTION = IllegalStateException("user not saved")
    }
}
