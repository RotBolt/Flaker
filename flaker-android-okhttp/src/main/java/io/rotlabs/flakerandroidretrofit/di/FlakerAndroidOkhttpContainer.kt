package io.rotlabs.flakerandroidretrofit.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.rotlabs.di.FlakerDataContainer
import io.rotlabs.flakerandroidretrofit.ui.FlakerViewModel
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore
import io.rotlabs.flakerretrofit.di.FlakerOkHttpCoreContainer

object FlakerAndroidOkhttpContainer {

    private var networkRequestRepo: NetworkRequestRepo? = null

    private var prefDataStore: PrefDataStore? = null

    private fun networkRequestRepo() = networkRequestRepo!!

    private fun prefDataStore() = prefDataStore!!

    fun install(appContext: Context) {
        FlakerOkHttpCoreContainer.install(FlakerDataContainer(appContext))
        networkRequestRepo = FlakerOkHttpCoreContainer.networkRequestRepo()
        prefDataStore = FlakerOkHttpCoreContainer.prefDataStore()
    }

    fun flakerViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val savedStateHandle = createSavedStateHandle()
            FlakerViewModel(networkRequestRepo(), prefDataStore(), savedStateHandle)
        }
    }
}
