package io.rotlabs.flakerretrofit

import io.rotlabs.di.FlakerDataContainer
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakedomain.prefs.FlakerPrefs
import io.rotlabs.flakedomain.prefs.RetentionPolicy
import io.rotlabs.flakerdb.networkrequest.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

class TestDataContainer : FlakerDataContainer(FakeContext()) {

    private val fakeNetworkRequestRepo = object : NetworkRequestRepo {
        override suspend fun selectAll(): List<NetworkRequest> {
            return emptyList()
        }

        override suspend fun insert(networkRequest: NetworkRequest) = Unit

        override fun observeAll(): Flow<List<NetworkRequest>> {
            return emptyFlow()
        }

        override suspend fun deleteExpiredData(retentionPolicy: RetentionPolicy) = Unit

        override suspend fun deleteAll() = Unit
    }

    private val fakePrefDataStore = object : PrefDataStore {

        private val prefFlow = MutableStateFlow(
            FlakerPrefs(
                shouldIntercept = false,
                delay = 0,
                failPercent = 0,
                variancePercent = 0,
                retentionPolicy = RetentionPolicy.THIRTY_DAYS
            )
        )

        override fun getPrefs(): Flow<FlakerPrefs> = flow {
            emit(prefFlow.value)
            emitAll(prefFlow)
        }

        override suspend fun savePrefs(flakerPrefs: FlakerPrefs) {
            prefFlow.emit(flakerPrefs)
        }
    }

    override val networkRequestRepo: NetworkRequestRepo
        get() = fakeNetworkRequestRepo

    override val prefsDataStore: PrefDataStore
        get() = fakePrefDataStore
}
