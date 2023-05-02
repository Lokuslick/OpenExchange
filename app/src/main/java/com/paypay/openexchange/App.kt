package com.paypay.openexchange

import android.app.Application
import com.paypay.openexchange.database.SharedPref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)

    }
}