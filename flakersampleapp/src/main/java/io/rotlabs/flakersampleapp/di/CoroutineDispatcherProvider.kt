package io.rotlabs.flakersampleapp.di

import kotlinx.coroutines.Dispatchers

object CoroutineDispatcherProvider {

    fun ioDispatcher() = Dispatchers.IO

    fun mainDispatcher() = Dispatchers.Main

    fun defaultDispatcher() = Dispatchers.Default
}
