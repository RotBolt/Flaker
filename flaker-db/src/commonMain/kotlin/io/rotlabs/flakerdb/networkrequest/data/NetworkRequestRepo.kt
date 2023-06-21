package io.rotlabs.flakerdb.networkrequest.data

import io.rotlabs.flakedomain.networkrequest.NetworkRequest

interface NetworkRequestRepo {
    fun selectAll(): List<NetworkRequest>
    fun insert(networkRequest: NetworkRequest)
}
