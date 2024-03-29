package io.github.bolt.flakersampleapp.di

import io.github.bolt.flakersampleapp.home.di.HomeContainer

class AppContainer {

    private val retrofitProvider: RetrofitProvider = RetrofitProvider()

    private var homeContainer: HomeContainer? = null

    fun initHomeContainer() {
        homeContainer = HomeContainer(retrofitProvider.provideRetrofit(), CoroutineDispatcherProvider)
    }

    fun releaseHomeContainer() {
        homeContainer = null
    }

    fun homeContainer(): HomeContainer = homeContainer ?: error("HomeContainer is not initialized")
}
