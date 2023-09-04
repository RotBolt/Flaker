package io.github.rotbolt.flakerandroidokhttp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.rotbolt.flakerandroidmonitor.FlakerMonitor
import io.github.rotbolt.flakerandroidmonitor.di.FlakerAndroidMonitorContainer
import io.github.rotbolt.flakerandroidokhttp.ui.FlakerViewModel
import io.github.rotbolt.flakerdata.di.FlakerDataContainer
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStore
import io.github.rotbolt.flakerokhttpcore.di.FlakerOkHttpCoreContainer

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
