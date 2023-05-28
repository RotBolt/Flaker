package io.rotlabs.flakerretrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

class FlakerInterceptor (
    private val context: Context,
    private val failResponse: FlakerFailResponse
): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = FlakerPrefs.instance(context)

        if (prefs.shouldIntercept()) {
            val behavior = NetworkBehavior.create();
            behavior.setDelay(prefs.getDelay(), TimeUnit.MILLISECONDS);
            behavior.setFailurePercent(prefs.getFailPercent());
            behavior.setVariancePercent(prefs.getVariancePercent());
            try {
                Thread.sleep(behavior.calculateDelay(TimeUnit.MILLISECONDS))
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            if (behavior.calculateIsFailure()) {
                // TODO add fail transaction from flaker
                return Response.Builder()
                    .code(failResponse.httpCode)
                    .protocol(Protocol.HTTP_1_1)
                    .message(failResponse.message)
                    .body(failResponse.responseBodyString.toResponseBody("text/plain".toMediaTypeOrNull()))
                    .request(chain.request())
                    .build()
            }


            // TODO add success transaction
            return chain.proceed(chain.request())


        } else {
           return chain.proceed(chain.request())
        }

    }

    public class Builder(private val context: Context) {
        private var failResponse: FlakerFailResponse = FlakerFailResponse()

        fun failResponse(response: FlakerFailResponse): Builder {
            failResponse = response
            return this
        }

        fun build(): FlakerInterceptor = FlakerInterceptor(
            context = context,
            failResponse = failResponse
        )
    }
}