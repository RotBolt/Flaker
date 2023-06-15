package io.rotlabs.flakerdb.networkrequest.data

import android.content.Context
import io.rotlabs.flakerdb.networkrequest.Network_request
import io.rotlabs.flakerdb.networkrequest.domain.NetworkRequest

class NoOpNetworkRequestRepoProvider(context: Context): NetworkRequestRepoProvider(context) {

    private val noOpNetworkRequestRepo = object : NetworkRequestRepo {
        override fun selectAll(): List<Network_request> {
            return emptyList()
        }

        override fun insert(networkRequest: NetworkRequest) {
            // do nothing
        }
    }
    override fun provide(): NetworkRequestRepo {
        return noOpNetworkRequestRepo
    }
}