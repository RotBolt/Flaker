package io.rotlabs.flakerretrofit.di

import io.rotlabs.di.FlakerDataContainer
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore

object FlakerOkHttpCoreContainer {

    private var networkRequestRepo: NetworkRequestRepo? = null

    private var prefDataStore: PrefDataStore? = null

    fun networkRequestRepo() = networkRequestRepo!!

    fun prefDataStore() = prefDataStore!!

    fun install(dataDependencyContainer: FlakerDataContainer) {
        networkRequestRepo = dataDependencyContainer.networkRequestRepo
        prefDataStore = dataDependencyContainer.prefsDataStore
    }
}
