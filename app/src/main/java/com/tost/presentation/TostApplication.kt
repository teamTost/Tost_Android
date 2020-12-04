package com.tost.presentation

import android.app.Application
import com.tost.config.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created By Malibin
 * on 12월 04, 2020
 */

class TostApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TostApplication)
            modules(diModules)
        }
    }
}