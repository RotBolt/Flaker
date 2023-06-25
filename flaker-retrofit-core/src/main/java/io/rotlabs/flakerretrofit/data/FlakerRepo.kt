package io.rotlabs.flakerretrofit.data

import android.content.Context
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoProvider

class FlakerRepo(private val context: Context) {
    internal constructor(
        context: Context,
        networkRequestRepoProvider: NetworkRequestRepoProvider
    ) : this(context) {
        this.networkRequestRepoProvider = networkRequestRepoProvider
    }

    private var networkRequestRepoProvider: NetworkRequestRepoProvider? = null

    private fun networkRequestRepoProvider(): NetworkRequestRepoProvider {
        return networkRequestRepoProvider ?: NetworkRequestRepoProvider(context)
    }

    private val networkRequestRepo by lazy { networkRequestRepoProvider().provide() }

    fun allRequests() = networkRequestRepo.selectAll()
}
