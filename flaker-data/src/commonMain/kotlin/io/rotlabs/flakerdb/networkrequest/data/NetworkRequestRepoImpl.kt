package io.rotlabs.flakerdb.networkrequest.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerdb.FlakerDatabase
import io.rotlabs.flakerprefs.RetentionPolicy
import io.rotlabs.flakerprefs.toMilliSeconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class NetworkRequestRepoImpl(
    sqlDriver: SqlDriver,
    private val dispatcher: CoroutineDispatcher
) : NetworkRequestRepo {

    private val db: FlakerDatabase = FlakerDatabase(sqlDriver)

    private val networkRequestQueries = db.networkRequestQueries

    override suspend fun selectAll() = withContext(dispatcher) {
        networkRequestQueries.selectAll().executeAsList().map {
            NetworkRequest(
                host = it.host,
                path = it.path,
                method = it.method,
                requestTime = it.request_time,
                responseCode = it.response_code,
                responseTimeTaken = it.response_time_taken,
                isFailedByFlaker = it.is_failed_by_flaker,
                createdAt = it.created_at
            )
        }
    }

    override suspend fun insert(networkRequest: NetworkRequest) {
        withContext(dispatcher) {
            networkRequestQueries.insert(
                id = null,
                host = networkRequest.host,
                path = networkRequest.path,
                method = networkRequest.method,
                request_time = networkRequest.requestTime,
                response_code = networkRequest.responseCode,
                response_time_taken = networkRequest.responseTimeTaken,
                is_failed_by_flaker = networkRequest.isFailedByFlaker,
                created_at = networkRequest.createdAt
            )
        }
    }

    override fun observeAll(): Flow<List<NetworkRequest>> {
        return networkRequestQueries.selectAll(
            mapper = { _,
                       host: String,
                       path: String,
                       method: String,
                       request_time: Long,
                       response_code: Long,
                       response_time_taken: Long,
                       is_failed_by_flaker: Boolean,
                       created_at: Long ->
                NetworkRequest(
                    host,
                    path,
                    method,
                    request_time,
                    response_code,
                    response_time_taken,
                    is_failed_by_flaker,
                    created_at
                )
            }
        ).asFlow().mapToList(dispatcher)
    }

    override suspend fun deleteExpiredData(retentionPolicy: RetentionPolicy) {
        withContext(dispatcher) {
            val currentMillis = Clock.System.now().toEpochMilliseconds()
            val expiryTimeMillis = currentMillis - retentionPolicy.toMilliSeconds()
            networkRequestQueries.deleteOldData(expiryTimeMillis)
        }
    }
}
