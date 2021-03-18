package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.tost.data.dao.get
import com.tost.data.entity.EntireGoal
import com.tost.data.entity.WeeklyGoal
import com.tost.data.service.TostService
import com.tost.data.service.request.SaveGoalRequestParams
import com.tost.data.service.response.GoalsResponse
import kotlinx.coroutines.flow.first
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
    suspend fun saveWeeklyGoal(token: String, weeklyGoal: WeeklyGoal) {
        tostService.saveWeeklyGoal(token, weeklyGoal.toParams())
        saveWeeklyGoalLocal(weeklyGoal)
    }

    suspend fun getWeeklyGoal(token: String): WeeklyGoal? {
        if (isLocalWeeklyGoalEmpty()) {
            return tostService.getGoals(token)
                .takeIf { it.isSuccessful }?.body()
                ?.also { saveGoalsLocal(it) }
                ?.getWeeklyGoal()
        }
        return WeeklyGoal(
            part1 = dataStore[KEY_GOAL_PART1].first() ?: return null,
            part2 = dataStore[KEY_GOAL_PART2].first() ?: return null,
            part3 = dataStore[KEY_GOAL_PART3].first() ?: return null,
            part4 = dataStore[KEY_GOAL_PART4].first() ?: return null,
            part5 = dataStore[KEY_GOAL_PART5].first() ?: return null,
            part6 = dataStore[KEY_GOAL_PART6].first() ?: return null,
        )
    }

    private suspend fun isLocalWeeklyGoalEmpty(): Boolean {
        return dataStore[KEY_GOAL_PART1].first() == null
    }

    suspend fun saveEntireGoal(token: String, entireGoal: EntireGoal) {
        tostService.saveGoal(token, SaveGoalRequestParams(entireGoal))
        saveEntireGoalLocal(entireGoal)
    }

    suspend fun getEntireGoal(token: String): EntireGoal? {
        if (isLocalEntireGoalEmpty()) {
            return tostService.getGoals(token)
                .takeIf { it.isSuccessful }?.body()
                ?.also { saveGoalsLocal(it) }
                ?.getEntireGoal()
        }
        return EntireGoal(
            level = dataStore[KEY_GOAL_LEVEL].first() ?: return null,
            startDate = Date(dataStore[KEY_GOAL_START_DATE].first() ?: return null),
            endDate = Date(dataStore[KEY_GOAL_END_DATE].first() ?: return null),
        )
    }

    private suspend fun isLocalEntireGoalEmpty(): Boolean {
        return dataStore[KEY_GOAL_LEVEL].first() == null
    }

    private suspend fun saveGoalsLocal(remoteGoals: GoalsResponse) {
        saveEntireGoalLocal(remoteGoals.getEntireGoal() ?: return)
        saveWeeklyGoalLocal(remoteGoals.getWeeklyGoal() ?: return)
    }

    private suspend fun saveEntireGoalLocal(entireGoal: EntireGoal) {
        dataStore.edit {
            it[KEY_GOAL_START_DATE] = entireGoal.startDate.time
            it[KEY_GOAL_END_DATE] = entireGoal.endDate.time
            it[KEY_GOAL_LEVEL] = entireGoal.level
        }
    }

    private suspend fun saveWeeklyGoalLocal(weeklyGoal: WeeklyGoal) {
        dataStore.edit {
            it[KEY_GOAL_PART1] = weeklyGoal.part1
            it[KEY_GOAL_PART2] = weeklyGoal.part2
            it[KEY_GOAL_PART3] = weeklyGoal.part3
            it[KEY_GOAL_PART4] = weeklyGoal.part4
            it[KEY_GOAL_PART5] = weeklyGoal.part5
            it[KEY_GOAL_PART6] = weeklyGoal.part6
        }
    }

    companion object {
        private val KEY_GOAL_START_DATE = preferencesKey<Long>("KEY_GOAL_START_DATE")
        private val KEY_GOAL_END_DATE = preferencesKey<Long>("KEY_GOAL_END_DATE")
        private val KEY_GOAL_LEVEL = preferencesKey<Int>("KEY_GOAL_LEVEL")

        private val KEY_GOAL_PART1 = preferencesKey<Int>("part1")
        private val KEY_GOAL_PART2 = preferencesKey<Int>("part2")
        private val KEY_GOAL_PART3 = preferencesKey<Int>("part3")
        private val KEY_GOAL_PART4 = preferencesKey<Int>("part4")
        private val KEY_GOAL_PART5 = preferencesKey<Int>("part5")
        private val KEY_GOAL_PART6 = preferencesKey<Int>("part6")
    }
}
