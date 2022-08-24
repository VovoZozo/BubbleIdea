package com.example.bubbleidea

import android.app.Application
import com.example.bubbleidea.di.AppComponent
import com.example.bubbleidea.di.DaggerAppComponent
import com.example.bubbleidea.di.DataBaseModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .dataBaseModule(DataBaseModule(this))
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}