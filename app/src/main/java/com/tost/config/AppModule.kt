package com.tost.config

import com.tost.data.repository.RecordsRepository
import com.tost.presentation.problem.part2.Part2ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

val repositoryModules = module {
    single { RecordsRepository(get()) }
}

val viewModelModules = module {
    viewModel { Part2ViewModel(get()) }
}

val diModules = listOf(
    retrofitModule,
    tostDataBaseModule,
    daoModule,
    repositoryModules,
    viewModelModules,
)