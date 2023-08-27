package io.rotlabs.flakerretrofit

import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerdb.networkrequest.NetworkRequestRepo
import io.rotlabs.flakerprefs.PrefDataStore
import io.rotlabs.flakerretrofit.di.FlakerOkHttpCoreContainer
import io.rotlabs.flakerretrofit.dto.FlakerFailResponse
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
    private val networkRequestRepo: NetworkRequestRepo
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val flakerPrefs = prefDataStore.getPrefs().first()

        if (flakerPrefs.shouldIntercept) {
            val behavior = NetworkBehavior.create()
            behavior.setDelay(flakerPrefs.delay.toLong(), TimeUnit.MILLISECONDS)
            behavior.setFailurePercent(flakerPrefs.failPercent)
            behavior.setVariancePercent(flakerPrefs.variancePercent)
            val calculatedDelay = behavior.calculateDelay(TimeUnit.MILLISECONDS)
            try {
                delay(calculatedDelay)
            } catch (e: InterruptedException) {
                // TODO add debug log later
            }

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
            return FlakerInterceptor(failResponse, prefDataStore, networkRequestRepo)
        }
    }
}
