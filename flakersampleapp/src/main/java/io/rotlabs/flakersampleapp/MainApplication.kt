package io.rotlabs.flakersampleapp

import android.app.Application
import io.rotlabs.flakersampleapp.di.AppContainer

class MainApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
