package io.rotlabs.flakerdb.networkrequest.data

import app.cash.sqldelight.db.SqlDriver
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerdb.FlakerDatabase

internal class NetworkRequestRepoImpl(sqlDriver: SqlDriver) : NetworkRequestRepo {

    private val db: FlakerDatabase = FlakerDatabase(sqlDriver)

    private val networkRequestQueries = db.networkRequestQueries

    override fun selectAll() = networkRequestQueries.selectAll().executeAsList().map {
        NetworkRequest(
            it.host,
            it.path,
            it.method,
            requestTime = it.request_time,
            responseCode = it.response_code,
            responseTimeTaken = it.response_time_taken,
            isFailedByFlaker = it.is_failed_by_flaker
        )
    }

    override fun insert(networkRequest: NetworkRequest) {
        networkRequestQueries.insert(
            id = null,
            host = networkRequest.host,
            path = networkRequest.path,
            method = networkRequest.method,
            request_time = networkRequest.requestTime,
            response_code = networkRequest.responseCode,
            response_time_taken = networkRequest.responseTimeTaken,
            is_failed_by_flaker = networkRequest.isFailedByFlaker
        )
    }
}
