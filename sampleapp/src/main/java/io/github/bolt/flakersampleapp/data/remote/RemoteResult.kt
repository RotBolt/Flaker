package io.github.bolt.flakersampleapp.data.remote

import retrofit2.HttpException
import retrofit2.Response

sealed class RemoteResult<out T : Any> {
    sealed class Success<out T : Any> : RemoteResult<T>() {
        data class Data<out T : Any>(val data: T) : Success<T>()
        object Empty : Success<Nothing>()
    }

    sealed class Error : RemoteResult<Nothing>() {
        object Remote : Error()
        object NotFound : Error()
        object AccessDenied : Error()
        object ServiceUnavailable : Error()
        object Unknown : Error()
    }
}

@Suppress("TooGenericExceptionCaught")
suspend fun <T : Any> handleRemoteResponse(
    execute: suspend () -> Response<T>
): RemoteResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful) {
            if (body != null) {
                RemoteResult.Success.Data(body)
            } else {
                RemoteResult.Success.Empty
            }
        } else {
            throw HttpException(response)
        }
    } catch (exception: Exception) {
        exception.toNetworkResult()
    }
}

private fun <T : Any> Exception.toNetworkResult(): RemoteResult<T> {
    return when (this) {
        is HttpException -> {
            when (code()) {
                HTTP_ACCESS_DENIED -> RemoteResult.Error.AccessDenied
                HTTP_NOT_FOUND -> RemoteResult.Error.NotFound
                HTTP_SERVICE_UNAVAILABLE -> RemoteResult.Error.ServiceUnavailable
                else -> RemoteResult.Error.Unknown
            }
        }
        else -> RemoteResult.Error.Remote
    }
}

private const val HTTP_ACCESS_DENIED = 401
private const val HTTP_NOT_FOUND = 404
private const val HTTP_SERVICE_UNAVAILABLE = 503
