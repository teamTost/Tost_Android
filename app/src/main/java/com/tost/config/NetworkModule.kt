package com.tost.config

import com.tost.data.service.TostService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

val retrofitModule = module {
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TostService.BASE_URL)
            .build()
            .create(TostService::class.java)
    }
}