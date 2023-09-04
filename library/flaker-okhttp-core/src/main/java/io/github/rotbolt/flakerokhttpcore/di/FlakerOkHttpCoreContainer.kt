package io.github.rotbolt.flakerokhttpcore.di

import io.github.rotbolt.flakerandroidmonitor.FlakerMonitor
import io.github.rotbolt.flakerandroidmonitor.di.FlakerAndroidMonitorContainer
import io.github.rotbolt.flakerdata.di.FlakerDataContainer
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStore

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
