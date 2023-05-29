package io.rotlabs.flakerdb.networkrequest.domain

data class NetworkRequest(
    val host: String,
    val path: String,
    val method: String,
    val requestTime: Long,
    val responseCode: Int,
    val responseTimeTaken: Long
)
