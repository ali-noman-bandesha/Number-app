package com.devsync.numberfinder.dependencyInjection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NumberApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}