package io.github.rotbolt.flakerdata.flakerdb.networkrequest

import io.github.rotbolt.flakedomain.networkrequest.NetworkRequest
import io.github.rotbolt.flakedomain.prefs.RetentionPolicy
import kotlinx.coroutines.flow.Flow

interface NetworkRequestRepo {
    suspend fun selectAll(): List<NetworkRequest>
    suspend fun insert(networkRequest: NetworkRequest)
    fun observeAll(): Flow<List<NetworkRequest>>
    suspend fun deleteExpiredData(retentionPolicy: RetentionPolicy)
    suspend fun deleteAll()
}
