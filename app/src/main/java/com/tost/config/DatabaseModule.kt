package com.tost.config

import androidx.room.Room
import com.tost.data.db.TostDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

val tostDataBaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), TostDataBase::class.java, "tostDatabase").build()
    }
}

val daoModule = module {
    single { get<TostDataBase>().recordsDao() }
}
