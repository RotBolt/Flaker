package io.rotlabs.flakerretrofit

import io.rotlabs.FlakerDataDependencyContainer
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore

object FlakerRetrofitDependencyContainer {

    private var networkRequestRepo: NetworkRequestRepo? = null

    private var prefDataStore: PrefDataStore? = null

    fun networkRequestRepo() = networkRequestRepo!!

    fun prefDataStore() = prefDataStore!!

    fun init(dataDependencyContainer: FlakerDataDependencyContainer) {
        networkRequestRepo = dataDependencyContainer.networkRequestRepo
        prefDataStore = dataDependencyContainer.prefsDataStore
    }
}
