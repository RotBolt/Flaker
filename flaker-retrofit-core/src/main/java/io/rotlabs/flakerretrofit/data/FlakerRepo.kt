package io.rotlabs.flakerretrofit.data

import android.content.Context
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoProvider

class FlakerRepo(private val context: Context) {

    internal constructor(
        context: Context,
        networkRequestRepoProvider: NetworkRequestRepoProvider,
        flakerPrefsProvider: FlakerPrefsProvider
    ) : this(context) {
        this.networkRequestRepoProvider = networkRequestRepoProvider
        this.flakerPrefsProvider = flakerPrefsProvider
    }

    private var networkRequestRepoProvider: NetworkRequestRepoProvider? = null

    private var flakerPrefsProvider: FlakerPrefsProvider? = null

    private fun networkRequestRepoProvider(): NetworkRequestRepoProvider {
        return networkRequestRepoProvider ?: NetworkRequestRepoProvider(context)
    }

    private fun flakerPrefsProvider(): FlakerPrefsProvider {
        return flakerPrefsProvider ?: FlakerPrefsProvider(context)
    }

    private val networkRequestRepo by lazy { networkRequestRepoProvider().provide() }

    private val flakerPrefs by lazy { flakerPrefsProvider().provide() }

    fun allRequests() = networkRequestRepo.selectAll()

    fun isFlakerOn() = flakerPrefs.shouldIntercept()
}
