package io.rotlabs.flakerdb.networkrequest.data

import app.cash.sqldelight.db.SqlDriver
import io.rotlabs.flakerdb.FlakerDatabase
import io.rotlabs.flakerdb.networkrequest.domain.NetworkRequest

class NetworkRequestRepo(sqlDriver: SqlDriver) {
    private val db: FlakerDatabase = FlakerDatabase(sqlDriver)

    private val networkRequestQueries = db.networkRequestQueries

    fun selectAll() = networkRequestQueries.selectAll()

    fun insert(networkRequest: NetworkRequest) {
        networkRequestQueries.insert(
            id = null,
            host = networkRequest.host,
            path = networkRequest.path,
            method = networkRequest.method,
            request_time = networkRequest.requestTime,
            response_code = networkRequest.responseCode.toLong(),
            response_time_taken = networkRequest.responseTimeTaken,
            is_failed_by_flaker = networkRequest.isFailedByFlaker
        )
    }
}