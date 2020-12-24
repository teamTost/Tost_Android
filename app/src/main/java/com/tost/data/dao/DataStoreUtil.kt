package com.tost.data.dao

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created By Malibin
 * on 12월 24, 2020
 */

operator fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): Flow<T?> {
    return this.data.map { it[key] }
}
