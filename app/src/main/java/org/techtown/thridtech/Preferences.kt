package org.techtown.thridtech

import android.app.Application

class Preferences : Application() {
    companion object {
        lateinit var prefs : PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}