package io.github.rotbolt.flakerdata.di

import android.content.Context
import io.github.rotbolt.flakerdata.flakerdb.DbDriverFactory
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepoImpl
import io.github.rotbolt.flakerdata.flakerprefs.DataStoreFactory
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStore
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStoreImpl
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