package com.tost.config

import com.tost.data.service.TostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created By Malibin
 * on 12월 13, 2020
 */

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideTostService(gsonConverterFactory: GsonConverterFactory): TostService {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(TostService.BASE_URL)
            .build()
            .create(TostService::class.java)
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}
