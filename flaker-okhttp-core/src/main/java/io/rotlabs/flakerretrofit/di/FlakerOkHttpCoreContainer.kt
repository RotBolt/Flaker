package io.rotlabs.flakerretrofit.di

import io.rotlabs.di.FlakerDataContainer
import io.rotlabs.flakerandroidmonitor.FlakerMonitor
import io.rotlabs.flakerandroidmonitor.di.FlakerAndroidMonitorContainer
import io.rotlabs.flakerdb.networkrequest.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore

object FlakerOkHttpCoreContainer {

    private var networkRequestRepo: NetworkRequestRepo? = null

    private var prefDataStore: PrefDataStore? = null

    private var flakerMonitor: FlakerMonitor? = null

    fun networkRequestRepo() = networkRequestRepo!!

    fun prefDataStore() = prefDataStore!!

    fun flakerMonitor() = flakerMonitor!!

    fun install(
        dataDependencyContainer: FlakerDataContainer,
        androidMonitorContainer: FlakerAndroidMonitorContainer,
    ) {
        networkRequestRepo = dataDependencyContainer.networkRequestRepo
        prefDataStore = dataDependencyContainer.prefsDataStore
        flakerMonitor = androidMonitorContainer.flakerMonitor
    }
}
