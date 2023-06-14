package io.rotlabs.flakerretrofit

import android.content.Context
import io.rotlabs.flakerdb.DriverFactory
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoImpl
import io.rotlabs.flakerdb.networkrequest.domain.NetworkRequest
import io.rotlabs.flakerretrofit.data.FlakerPrefs
import io.rotlabs.flakerretrofit.data.FlakerPrefsImpl
import io.rotlabs.flakerretrofit.domain.FlakerFailResponse
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

class FlakerInterceptor internal constructor(
    private val failResponse: FlakerFailResponse,
    private val flakerPrefs: FlakerPrefs,
    private val networkRequestRepo: NetworkRequestRepo
): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        if (flakerPrefs.shouldIntercept()) {
            val behavior = NetworkBehavior.create();
            behavior.setDelay(flakerPrefs.getDelay(), TimeUnit.MILLISECONDS);
            behavior.setFailurePercent(flakerPrefs.getFailPercent());
            behavior.setVariancePercent(flakerPrefs.getVariancePercent());
            try {
                Thread.sleep(behavior.calculateDelay(TimeUnit.MILLISECONDS))
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val request = chain.request()

            if (behavior.calculateIsFailure()) {
                val flakerInterceptedResponse =  Response.Builder()
                    .code(failResponse.httpCode)
                    .protocol(Protocol.HTTP_1_1)
                    .message(failResponse.message)
                    .body(failResponse.responseBodyString.toResponseBody("text/plain".toMediaTypeOrNull()))
                    .request(chain.request())
                    .build()

                saveNetworkTransaction(request, flakerInterceptedResponse)
                return flakerInterceptedResponse
            }

            val nonFlakerInterceptedResponse =  chain.proceed(chain.request())
            saveNetworkTransaction(request, nonFlakerInterceptedResponse)
            return nonFlakerInterceptedResponse


        } else {
           return chain.proceed(chain.request())
        }

    }

    private fun saveNetworkTransaction(request: Request, response: Response) {
        val networkRequest = NetworkRequest(
            host = request.url.host,
            path = request.url.pathSegments.joinToString("/"),
            method = request.method,
            requestTime = response.sentRequestAtMillis,
            responseCode = response.code,
            responseTimeTaken = response.receivedResponseAtMillis,
            isFailedByFlaker = true
        )

        networkRequestRepo.insert(networkRequest)
    }

    public class Builder(private val context: Context) {
        private var failResponse: FlakerFailResponse = FlakerFailResponse()

        fun failResponse(response: FlakerFailResponse): Builder {
            failResponse = response
            return this
        }
        fun build(): FlakerInterceptor {
            val networkRequestRepo = NetworkRequestRepoImpl(DriverFactory(context).createDriver())
            val flakerPrefs : FlakerPrefs = FlakerPrefsImpl.instance(context)
            return FlakerInterceptor(failResponse, flakerPrefs, networkRequestRepo)
        }
    }
}