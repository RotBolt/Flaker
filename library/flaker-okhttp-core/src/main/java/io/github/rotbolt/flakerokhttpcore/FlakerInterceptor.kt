package io.github.rotbolt.flakerokhttpcore

import io.github.rotbolt.flakedomain.networkrequest.NetworkRequest
import io.github.rotbolt.flakerandroidmonitor.FlakerMonitor
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStore
import io.github.rotbolt.flakerokhttpcore.di.FlakerOkHttpCoreContainer
import io.github.rotbolt.flakerokhttpcore.dto.FlakerFailResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

class FlakerInterceptor private constructor(
    private val failResponse: FlakerFailResponse,
    private val prefDataStore: PrefDataStore,
    private val networkRequestRepo: NetworkRequestRepo,
    private val flakerMonitor: FlakerMonitor,
) : Interceptor {

    @Suppress("TooGenericExceptionCaught")
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        try {
            val flakerPrefs = prefDataStore.getPrefs().first()

            if (flakerPrefs.shouldIntercept) {
                val behavior = NetworkBehavior.create()
                behavior.setDelay(flakerPrefs.delay.toLong(), TimeUnit.MILLISECONDS)
                behavior.setFailurePercent(flakerPrefs.failPercent)
                behavior.setVariancePercent(flakerPrefs.variancePercent)
                val calculatedDelay = behavior.calculateDelay(TimeUnit.MILLISECONDS)
                delay(calculatedDelay)
                val request = chain.request()

                if (behavior.calculateIsFailure()) {
                    val flakerInterceptedResponse = Response.Builder()
                        .code(failResponse.httpCode)
                        .protocol(Protocol.HTTP_1_1)
                        .message(failResponse.message)
                        .body(failResponse.responseBodyString.toResponseBody("text/plain".toMediaTypeOrNull()))
                        .request(chain.request())
                        .sentRequestAtMillis(System.currentTimeMillis())
                        .receivedResponseAtMillis(System.currentTimeMillis())
                        .build()

                    saveNetworkTransaction(request, flakerInterceptedResponse, calculatedDelay, true)
                    return@runBlocking flakerInterceptedResponse
                }

                val nonFlakerInterceptedResponse = chain.proceed(chain.request())
                saveNetworkTransaction(request, nonFlakerInterceptedResponse, calculatedDelay, false)
                return@runBlocking nonFlakerInterceptedResponse
            } else {
                return@runBlocking chain.proceed(chain.request())
            }
        } catch (e: Exception) {
            flakerMonitor.captureException(
                e,
                mapOf(TAG to "Error while intercepting network request. Resuming normal flow: ${e.message}")
            )
            return@runBlocking chain.proceed(chain.request())
        }
    }

    private suspend fun saveNetworkTransaction(
        request: Request,
        response: Response,
        delay: Long,
        isFailedByFlaker: Boolean
    ) {
        val networkRequest = NetworkRequest(
            host = request.url.host,
            path = request.url.pathSegments.joinToString("/"),
            method = request.method,
            requestTime = response.sentRequestAtMillis,
            responseCode = response.code.toLong(),
            responseTimeTaken = (response.receivedResponseAtMillis - response.sentRequestAtMillis) + delay,
            isFailedByFlaker = isFailedByFlaker,
            createdAt = System.currentTimeMillis()
        )

        networkRequestRepo.insert(networkRequest)
    }

    class Builder {

        private var failResponse: FlakerFailResponse = FlakerFailResponse()

        fun failResponse(response: FlakerFailResponse): Builder {
            failResponse = response
            return this
        }

        fun build(): FlakerInterceptor {
            val networkRequestRepo = FlakerOkHttpCoreContainer.networkRequestRepo()
            val prefDataStore = FlakerOkHttpCoreContainer.prefDataStore()
            val flakerMonitor = FlakerOkHttpCoreContainer.flakerMonitor()
            return FlakerInterceptor(failResponse, prefDataStore, networkRequestRepo, flakerMonitor)
        }
    }

    companion object {
        private const val TAG = "FlakerInterceptor"
    }
}
