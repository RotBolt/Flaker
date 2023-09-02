package io.rotlabs.flakerdb.networkrequest

import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakedomain.prefs.RetentionPolicy
import io.rotlabs.flakerdb.testDbDriverFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class InMemoryNetworkRequestRepo(testDispatcher: CoroutineDispatcher) : NetworkRequestRepo {

    private val actualInMemoryRepo = NetworkRequestRepoImpl(
        testDbDriverFactory(),
        testDispatcher
    )
    override suspend fun selectAll(): List<NetworkRequest> = actualInMemoryRepo.selectAll()

    override suspend fun insert(networkRequest: NetworkRequest) = actualInMemoryRepo.insert(networkRequest)

    override fun observeAll(): Flow<List<NetworkRequest>> = actualInMemoryRepo.observeAll()

    override suspend fun deleteExpiredData(retentionPolicy: RetentionPolicy) {
        return actualInMemoryRepo.deleteExpiredData(retentionPolicy)
    }

    override suspend fun deleteAll() = actualInMemoryRepo.deleteAll()
}
