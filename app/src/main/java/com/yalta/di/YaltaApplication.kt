package com.yalta.di

import android.app.Application
import android.content.Context

class YaltaApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
        setContext()
    }

    private fun setContext() {
        context = applicationContext
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.builder()
            .build()
    }
}
