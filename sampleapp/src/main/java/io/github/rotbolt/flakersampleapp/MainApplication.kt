package io.github.rotbolt.flakersampleapp

import android.app.Application
import io.github.rotbolt.flakerandroidokhttp.di.FlakerAndroidOkhttpContainer
import io.github.rotbolt.flakersampleapp.di.AppContainer

class MainApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        FlakerAndroidOkhttpContainer.install(this)
        appContainer = AppContainer()
    }
}
