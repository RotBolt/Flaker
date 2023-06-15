package io.rotlabs.flakerdb.networkrequest.data

import android.content.Context
import io.rotlabs.flakerdb.DriverFactory

open class NetworkRequestRepoProvider(private val context: Context) {
    open fun provide() : NetworkRequestRepo = NetworkRequestRepoImpl(DriverFactory(context).createDriver())
}