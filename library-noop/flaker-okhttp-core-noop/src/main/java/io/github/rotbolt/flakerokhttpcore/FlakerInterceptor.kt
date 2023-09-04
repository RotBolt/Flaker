package io.github.rotbolt.flakerokhttpcore

import io.github.rotbolt.flakerokhttpcore.dto.FlakerFailResponse
import okhttp3.Interceptor
import okhttp3.Response

class FlakerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }

    class Builder {

        private var failResponse: FlakerFailResponse = FlakerFailResponse()

        fun failResponse(response: FlakerFailResponse): Builder {
            failResponse = response
            return this
        }

        fun build(): FlakerInterceptor = FlakerInterceptor()
    }
}
