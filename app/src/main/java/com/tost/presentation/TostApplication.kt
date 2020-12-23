package com.tost.presentation

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
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

        Logger.addLogAdapter(AndroidLogAdapter(getLoggerFormatStrategy()))
        Timber.plant(LoggerEntangledTree())
    }

    private fun getLoggerFormatStrategy(): FormatStrategy = PrettyFormatStrategy.newBuilder()
        .methodCount(0)
        .tag("Tost")
        .build()

    private class LoggerEntangledTree : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            Logger.log(priority, tag, message, t)
        }
    }
}
