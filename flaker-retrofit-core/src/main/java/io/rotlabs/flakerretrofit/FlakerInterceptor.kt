package io.rotlabs.flakerretrofit

import android.content.Context
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoProvider
import io.rotlabs.flakerretrofit.data.FlakerPrefs
import io.rotlabs.flakerretrofit.data.FlakerPrefsProvider
import io.rotlabs.flakerretrofit.domain.FlakerFailResponse
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
    private val flakerPrefs: FlakerPrefs,
    private val networkRequestRepo: NetworkRequestRepo
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (flakerPrefs.shouldIntercept()) {
            val behavior = NetworkBehavior.create()
            behavior.setDelay(flakerPrefs.getDelay(), TimeUnit.MILLISECONDS)
            behavior.setFailurePercent(flakerPrefs.getFailPercent())
            behavior.setVariancePercent(flakerPrefs.getVariancePercent())
            val calculatedDelay = behavior.calculateDelay(TimeUnit.MILLISECONDS)
            try {
                Thread.sleep(calculatedDelay)
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
                    .sentRequestAtMillis(System.currentTimeMillis() - calculatedDelay)
                    .receivedResponseAtMillis(System.currentTimeMillis())
                    .build()

                saveNetworkTransaction(request, flakerInterceptedResponse, true)
                return flakerInterceptedResponse
            }

            val nonFlakerInterceptedResponse = chain.proceed(chain.request())
            saveNetworkTransaction(request, nonFlakerInterceptedResponse, false)
            return nonFlakerInterceptedResponse
        } else {
            return chain.proceed(chain.request())
        }
    }

    private fun saveNetworkTransaction(request: Request, response: Response, isFailedByFlaker: Boolean) {
        val networkRequest = NetworkRequest(
            host = request.url.host,
            path = request.url.pathSegments.joinToString("/"),
            method = request.method,
            requestTime = response.sentRequestAtMillis,
            responseCode = response.code.toLong(),
            responseTimeTaken = response.receivedResponseAtMillis,
            isFailedByFlaker = isFailedByFlaker
        )

        networkRequestRepo.insert(networkRequest)
    }

    class Builder(private val context: Context) {

        private var failResponse: FlakerFailResponse = FlakerFailResponse()
        private var networkRequestRepoProvider: NetworkRequestRepoProvider? = null
        private var flakerPrefsProvider: FlakerPrefsProvider? = null

        private fun networkRequestRepoProvider(): NetworkRequestRepoProvider {
            return networkRequestRepoProvider ?: NetworkRequestRepoProvider(context)
        }

        private fun flakerPrefsProvider(): FlakerPrefsProvider = flakerPrefsProvider ?: FlakerPrefsProvider(context)

        internal constructor(
            context: Context,
            networkRequestRepoProvider: NetworkRequestRepoProvider,
            flakerPrefsProvider: FlakerPrefsProvider
        ) : this(context) {
            this.networkRequestRepoProvider = networkRequestRepoProvider
            this.flakerPrefsProvider = flakerPrefsProvider
        }

        fun failResponse(response: FlakerFailResponse): Builder {
            failResponse = response
            return this
        }

        fun build(): FlakerInterceptor {
            val networkRequestRepo = networkRequestRepoProvider().provide()
            val flakerPrefs: FlakerPrefs = flakerPrefsProvider().provide()
            return FlakerInterceptor(failResponse, flakerPrefs, networkRequestRepo)
        }
    }
}
