package io.rotlabs.flakerdb.networkrequest

import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakedomain.prefs.RetentionPolicy
import kotlinx.coroutines.flow.Flow

interface NetworkRequestRepo {
    suspend fun selectAll(): List<NetworkRequest>
    suspend fun insert(networkRequest: NetworkRequest)
    fun observeAll(): Flow<List<NetworkRequest>>
    suspend fun deleteExpiredData(retentionPolicy: RetentionPolicy)
    suspend fun deleteAll()
}
