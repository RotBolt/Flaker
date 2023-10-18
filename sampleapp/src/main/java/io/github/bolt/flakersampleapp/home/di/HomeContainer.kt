package io.github.bolt.flakersampleapp.home.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.bolt.flakersampleapp.di.CoroutineDispatcherProvider
import io.github.bolt.flakersampleapp.home.HomeViewModel
import io.github.bolt.flakersampleapp.home.data.remote.UsersApiClient
import io.github.bolt.flakersampleapp.home.data.remote.UsersApiService
import retrofit2.Retrofit

class HomeContainer(
    private val retrofit: Retrofit,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) {

    private val usersApiService: UsersApiService
        get() = retrofit.create(UsersApiService::class.java)

    private fun userApiClient(): UsersApiClient = UsersApiClient(
        usersApiService,
        coroutineDispatcherProvider.ioDispatcher()
    )

    val homeViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val savedStateHandle = createSavedStateHandle()
            HomeViewModel(userApiClient(), savedStateHandle)
        }
    }
}
