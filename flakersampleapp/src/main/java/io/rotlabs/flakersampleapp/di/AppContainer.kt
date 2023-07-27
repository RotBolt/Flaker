package io.rotlabs.flakersampleapp.di

import android.content.Context
import io.rotlabs.flakersampleapp.home.di.HomeContainer

class AppContainer(appContext: Context) {

    private val retrofitProvider: RetrofitProvider = RetrofitProvider(appContext)

    private var homeContainer: HomeContainer? = null

    fun initHomeContainer() {
        homeContainer = HomeContainer(retrofitProvider.provideRetrofit(), CoroutineDispatcherProvider)
    }

    fun releaseHomeContainer() {
        homeContainer = null
    }

    fun homeContainer(): HomeContainer = homeContainer ?: error("HomeContainer is not initialized")
}
