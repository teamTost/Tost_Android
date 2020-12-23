package com.tost.config

import com.tost.data.service.TostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
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
    fun provideTostService(): TostService {
        return Retrofit.Builder()
            .addConverterFactory(createGsonConverterFactory())
            .client(createOkHttpClient())
            .baseUrl(TostService.BASE_URL)
            .build()
            .create(TostService::class.java)
    }

    private fun createGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createHttpLoggingInterceptor())
            .build()
    }

    private fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { Timber.tag("OK_HTTP").d(it) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    }
}
