package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.tost.data.service.TostService
import com.tost.data.service.request.SaveGoalRequestParams
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

/**
 * Created By Malibin
 * on 12월 23, 2020
 */

class GoalRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val tostService: TostService,
) {
    suspend fun saveGoal(token: String, date: Date, level: Int) {
        tostService.saveGoal(token, SaveGoalRequestParams(date, level))
        dataStore.edit {
            it[KEY_GOAL_DATE] = date.time
            it[KEY_GOAL_LEVEL] = level
        }
    }

    suspend fun getDate(): Date? {
        val time = dataStore.data.map { it[KEY_GOAL_DATE] }.first() ?: return null
        return Date(time)
    }

    suspend fun getLevel(): Int? {
        return dataStore.data.map { it[KEY_GOAL_LEVEL] }.first()
    }

    companion object {
        private val KEY_GOAL_DATE = preferencesKey<Long>("KEY_GOAL_DATE")
        private val KEY_GOAL_LEVEL = preferencesKey<Int>("KEY_GOAL_LEVEL")
    }
}
