package com.tost.config

import com.tost.data.repository.RecordsRepository
import org.koin.dsl.module

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

val repositoryModules = module {
    single { RecordsRepository(get()) }
}

val diModules = listOf(
    tostDataBaseModule,
    daoModule,
    repositoryModules,
)