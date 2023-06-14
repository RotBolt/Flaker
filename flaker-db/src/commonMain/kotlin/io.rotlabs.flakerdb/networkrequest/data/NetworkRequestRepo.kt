package io.rotlabs.flakerdb.networkrequest.data

import io.rotlabs.flakerdb.networkrequest.Network_request
import io.rotlabs.flakerdb.networkrequest.domain.NetworkRequest

interface NetworkRequestRepo {
    fun selectAll(): List<Network_request>
    fun insert(networkRequest: NetworkRequest)
}