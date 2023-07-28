package io.rotlabs

import android.content.Context
import io.rotlabs.flakerdb.DbDriverFactory
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoImpl
import io.rotlabs.flakerprefs.DataStoreFactory
import io.rotlabs.flakerprefs.PrefDataStore
import io.rotlabs.flakerprefs.PrefDataStoreImpl

open class FlakerDataDependencyContainer(context: Context) {

    open val networkRequestRepo : NetworkRequestRepo by lazy {
        NetworkRequestRepoImpl(DbDriverFactory(context).createDriver())
    }

    open val prefsDataStore : PrefDataStore by lazy {
        PrefDataStoreImpl(DataStoreFactory(context).create())
    }

}