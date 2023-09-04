package io.github.rotbolt.flakerokhttpcore

import io.github.rotbolt.flakedomain.networkrequest.NetworkRequest
import io.github.rotbolt.flakedomain.prefs.FlakerPrefs
import io.github.rotbolt.flakedomain.prefs.RetentionPolicy
import io.github.rotbolt.flakerdata.di.FlakerDataContainer
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStore
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
