package io.github.bolt.flakersampleapp

import android.app.Application
import io.github.bolt.flakersampleapp.di.AppContainer
import io.github.rotbolt.flakerandroidokhttp.di.FlakerAndroidOkhttpContainer

class MainApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        FlakerAndroidOkhttpContainer.install(this)
        appContainer = AppContainer()
    }
}
