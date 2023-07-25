package io.rotlabs.flakerdb.networkrequest.data

import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import kotlinx.coroutines.flow.Flow

interface NetworkRequestRepo {
    fun selectAll(): List<NetworkRequest>
    fun insert(networkRequest: NetworkRequest)
    fun observeAll(): Flow<List<NetworkRequest>>
}
