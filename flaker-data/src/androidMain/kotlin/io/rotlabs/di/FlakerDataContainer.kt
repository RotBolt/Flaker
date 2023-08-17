package io.rotlabs.di

import android.content.Context
import io.rotlabs.flakerdb.DbDriverFactory
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoImpl
import io.rotlabs.flakerprefs.DataStoreFactory
import io.rotlabs.flakerprefs.PrefDataStore
import io.rotlabs.flakerprefs.PrefDataStoreImpl
import kotlinx.coroutines.Dispatchers

open class FlakerDataContainer(context: Context) {

    private val ioDispatcher
        get() = Dispatchers.IO

    open val networkRequestRepo : NetworkRequestRepo by lazy {
        NetworkRequestRepoImpl(DbDriverFactory(context).createDriver(), ioDispatcher)
    }

    open val prefsDataStore : PrefDataStore by lazy {
        PrefDataStoreImpl(DataStoreFactory(context).create())
    }

}