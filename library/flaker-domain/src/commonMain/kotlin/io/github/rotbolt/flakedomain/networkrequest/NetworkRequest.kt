package io.github.rotbolt.flakedomain.networkrequest

data class NetworkRequest(
    val host: String,
    val path: String,
    val method: String,
    val requestTime: Long,
    val responseCode: Long,
    val responseTimeTaken: Long,
    val isFailedByFlaker: Boolean,
    val createdAt: Long
)
