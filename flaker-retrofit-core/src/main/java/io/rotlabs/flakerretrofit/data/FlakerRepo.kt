package io.rotlabs.flakerretrofit.data

import android.content.Context
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FlakerRepo(private val context: Context) {

    internal constructor(
        context: Context,
        networkRequestRepoProvider: NetworkRequestRepoProvider,
        flakerPrefsProvider: FlakerPrefsProvider,
        coroutineDispatcher: CoroutineDispatcher,
    ) : this(context) {
        this.networkRequestRepoProvider = networkRequestRepoProvider
        this.flakerPrefsProvider = flakerPrefsProvider
        this.coroutineDispatcher = coroutineDispatcher
    }

    private var networkRequestRepoProvider: NetworkRequestRepoProvider? = null

    private var flakerPrefsProvider: FlakerPrefsProvider? = null

    private var coroutineDispatcher: CoroutineDispatcher? = null

    private fun networkRequestRepoProvider(): NetworkRequestRepoProvider {
        return networkRequestRepoProvider ?: NetworkRequestRepoProvider(context)
    }

    private fun flakerPrefsProvider(): FlakerPrefsProvider {
        return flakerPrefsProvider ?: FlakerPrefsProvider(context)
    }

    // TODO Add Provider for this as well
    private fun coroutineDispatcher(): CoroutineDispatcher {
        return coroutineDispatcher ?: Dispatchers.IO
    }

    private val networkRequestRepo by lazy { networkRequestRepoProvider().provide() }

    private val flakerPrefs by lazy { flakerPrefsProvider().provide() }

    private val dispatcher by lazy { coroutineDispatcher() }

    suspend fun allRequests() = withContext(dispatcher) { networkRequestRepo.selectAll() }

    fun observeAllRequests() = networkRequestRepo.observeAll().flowOn(dispatcher)

    fun isFlakerOn() = flakerPrefs.shouldIntercept()

    fun observeFlakerOn() = flakerPrefs.getShouldInterceptFlow().flowOn(dispatcher)

    fun saveDelayValue(delay: Long) = flakerPrefs.saveDelay(delay)

    fun saveFailPercent(failPercent: Int) = flakerPrefs.saveFailPercent(failPercent)

    fun saveVariancePercent(variancePercent: Int) = flakerPrefs.saveVariancePercent(variancePercent)

    fun saveShouldIntercept(shouldIntercept: Boolean) = flakerPrefs.saveShouldIntercept(shouldIntercept)
}
