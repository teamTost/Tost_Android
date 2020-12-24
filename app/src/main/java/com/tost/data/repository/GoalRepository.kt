package com.tost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.tost.data.dao.get
import com.tost.data.entity.Part
import com.tost.data.entity.WeeklyGoal
import com.tost.data.service.TostService
import com.tost.data.service.request.SaveGoalRequestParams
import kotlinx.coroutines.flow.Flow
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
    suspend fun saveWeeklyGoal(weeklyGoal: WeeklyGoal) {
        dataStore.edit {
            it[KEY_GOAL_PART1] = weeklyGoal.part1
            it[KEY_GOAL_PART2] = weeklyGoal.part2
            it[KEY_GOAL_PART3] = weeklyGoal.part3
            it[KEY_GOAL_PART4] = weeklyGoal.part4
            it[KEY_GOAL_PART5] = weeklyGoal.part5
            it[KEY_GOAL_PART6] = weeklyGoal.part6
        }
    }

    suspend fun getWeeklyGoal(): WeeklyGoal? {
        return WeeklyGoal(
            part1 = dataStore[KEY_GOAL_PART1].first() ?: return null,
            part2 = dataStore[KEY_GOAL_PART2].first() ?: return null,
            part3 = dataStore[KEY_GOAL_PART3].first() ?: return null,
            part4 = dataStore[KEY_GOAL_PART4].first() ?: return null,
            part5 = dataStore[KEY_GOAL_PART5].first() ?: return null,
            part6 = dataStore[KEY_GOAL_PART6].first() ?: return null,
        )
    }

    suspend fun saveGoal(token: String, date: Date, level: Int) {
        tostService.saveGoal(token, SaveGoalRequestParams(date, level))
        dataStore.edit {
            it[KEY_GOAL_DATE] = date.time
            it[KEY_GOAL_LEVEL] = level
        }
    }

    suspend fun getDate(): Date? {
        val time = dataStore[KEY_GOAL_DATE].first() ?: return null
        return Date(time)
    }

    suspend fun getLevel(): Int? {
        return dataStore[KEY_GOAL_LEVEL].first()
    }

    companion object {
        private val KEY_GOAL_DATE = preferencesKey<Long>("KEY_GOAL_DATE")
        private val KEY_GOAL_LEVEL = preferencesKey<Int>("KEY_GOAL_LEVEL")

        private val KEY_GOAL_PART1 = preferencesKey<Int>("part1")
        private val KEY_GOAL_PART2 = preferencesKey<Int>("part2")
        private val KEY_GOAL_PART3 = preferencesKey<Int>("part3")
        private val KEY_GOAL_PART4 = preferencesKey<Int>("part4")
        private val KEY_GOAL_PART5 = preferencesKey<Int>("part5")
        private val KEY_GOAL_PART6 = preferencesKey<Int>("part6")
    }
}
