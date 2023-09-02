package io.rotlabs.flakerandroidokhttp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.rotlabs.di.FlakerDataContainer
import io.rotlabs.flakerandroidmonitor.FlakerMonitor
import io.rotlabs.flakerandroidmonitor.di.FlakerAndroidMonitorContainer
import io.rotlabs.flakerandroidokhttp.ui.FlakerViewModel
import io.rotlabs.flakerdb.networkrequest.NetworkRequestRepo
import io.rotlabs.flakerokhttpcore.di.FlakerOkHttpCoreContainer
import io.rotlabs.flakerprefs.PrefDataStore

object FlakerAndroidOkhttpContainer {

    private var networkRequestRepo: NetworkRequestRepo? = null

    private var prefDataStore: PrefDataStore? = null

    private var flakerMonitor: FlakerMonitor? = null

    private fun networkRequestRepo() = networkRequestRepo!!

    private fun prefDataStore() = prefDataStore!!

    private fun flakerMonitor() = flakerMonitor!!

    fun install(appContext: Context) {
        val flakerAndroidMonitorContainer = FlakerAndroidMonitorContainer(appContext)
        FlakerOkHttpCoreContainer.install(FlakerDataContainer(appContext), flakerAndroidMonitorContainer)
        networkRequestRepo = FlakerOkHttpCoreContainer.networkRequestRepo()
        prefDataStore = FlakerOkHttpCoreContainer.prefDataStore()
        flakerMonitor = flakerAndroidMonitorContainer.flakerMonitor
    }

    fun flakerViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val savedStateHandle = createSavedStateHandle()
            FlakerViewModel(networkRequestRepo(), prefDataStore(), flakerMonitor(), savedStateHandle)
        }
    }
}
