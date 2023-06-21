package io.rotlabs.flakerdb.networkrequest.data

import android.content.Context
import io.rotlabs.flakedomain.networkrequest.NetworkRequest

class NoOpNetworkRequestRepoProvider(context: Context): NetworkRequestRepoProvider(context) {

    private val noOpNetworkRequestRepo = object : NetworkRequestRepo {
        override fun selectAll(): List<NetworkRequest> {
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