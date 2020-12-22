package com.tost.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

@HiltAndroidApp
class TostApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}
