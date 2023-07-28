package io.rotlabs.flakersampleapp.di

import kotlinx.coroutines.Dispatchers

@Suppress("InjectDispatcher")
object CoroutineDispatcherProvider {

    fun ioDispatcher() = Dispatchers.IO

    fun mainDispatcher() = Dispatchers.Main

    fun defaultDispatcher() = Dispatchers.Default
}
