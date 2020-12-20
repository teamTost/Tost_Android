package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.tost.data.entity.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 20, 2020
 */

class UserRepository @Inject constructor(
    private val userDataStore: DataStore<Preferences>
) {
    suspend fun saveUser(user: User) {
        userDataStore.edit {
            it[KEY_EMAIL] = user.email
            it[KEY_NAME] = user.name
            it[KEY_PROFILE_URL] = user.profileUrl
            it[KEY_GOOGLE_TOKEN] = user.googleToken
            it[KEY_TOST_TOKEN] = user.tostToken
        }
    }

    suspend fun getUser(): User = User(
        email = getEmail(),
        name = getName(),
        profileUrl = getProfileUrl(),
        googleToken = getGoogleToken(),
        tostToken = getTostToken(),
    )

    suspend fun getEmail(): String {
        return userDataStore.data.map { it[KEY_EMAIL] }.first().orEmpty()
    }

    suspend fun getName(): String {
        return userDataStore.data.map { it[KEY_NAME] }.first().orEmpty()
    }

    suspend fun getProfileUrl(): String {
        return userDataStore.data.map { it[KEY_PROFILE_URL] }.first().orEmpty()
    }

    suspend fun getGoogleToken(): String {
        return userDataStore.data.map { it[KEY_GOOGLE_TOKEN] }.first().orEmpty()
    }

    suspend fun getTostToken(): String {
        return userDataStore.data.map { it[KEY_TOST_TOKEN] }.first().orEmpty()
    }

    companion object {
        private val KEY_EMAIL = preferencesKey<String>("KEY_EMAIL")
        private val KEY_NAME = preferencesKey<String>("KEY_NAME")
        private val KEY_PROFILE_URL = preferencesKey<String>("KEY_PROFILE_URL")
        private val KEY_GOOGLE_TOKEN = preferencesKey<String>("KEY_GOOGLE_TOKEN")
        private val KEY_TOST_TOKEN = preferencesKey<String>("KEY_TOST_TOKEN")
    }
}
