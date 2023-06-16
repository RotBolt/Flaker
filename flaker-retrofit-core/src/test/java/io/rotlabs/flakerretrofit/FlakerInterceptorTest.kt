package io.rotlabs.flakerretrofit

import io.rotlabs.flakerdb.networkrequest.data.NoOpNetworkRequestRepoProvider
import io.rotlabs.flakerretrofit.data.TestFlakerPrefsProvider
import io.rotlabs.flakerretrofit.domain.FlakerFailResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test

class FlakerInterceptorTest {

    private val clientBuilder = OkHttpClient.Builder()

    private lateinit var flakerInterceptor: FlakerInterceptor

    private val flakerFailResponse = FlakerFailResponse()

    private val fakeContext = FakeContext()

    private val testNetworkRequestRepoProvider = NoOpNetworkRequestRepoProvider(fakeContext)

    private val testFlakerPrefsProvider = TestFlakerPrefsProvider(fakeContext)

    @Test
    fun `GIVEN shouldIntercept true, fail rate 100 WHEN request intercepted THEN it should give fail response`() {
        testFlakerPrefsProvider.setVersion(TestFlakerPrefsProvider.Version.FAKE_FAIL)

        flakerInterceptor = FlakerInterceptor.Builder(
            fakeContext,
            testNetworkRequestRepoProvider,
            testFlakerPrefsProvider
        )
            .failResponse(flakerFailResponse)
            .build()

        val request = Request.Builder().url("https://localhost:8080/").build()

        val client = clientBuilder.addInterceptor(flakerInterceptor).build()

        val response = client.newCall(request).execute()
        assert(response.code == flakerFailResponse.httpCode)
        assert(response.message == flakerFailResponse.message)
    }

    @Test
    fun `GIVEN flakerPrefs shouldIntercept false WHEN request intercepted THEN it should give server response`() {
        testFlakerPrefsProvider.setVersion(TestFlakerPrefsProvider.Version.FAKE_NO_INTERCEPT)

        flakerInterceptor = FlakerInterceptor.Builder(
            fakeContext,
            testNetworkRequestRepoProvider,
            testFlakerPrefsProvider
        )
            .failResponse(flakerFailResponse)
            .build()

        val server = MockWebServer()
        server.enqueue(MockResponse().setResponseCode(200).setBody("Success"))

        server.start()
        val httpUrl = server.url("/")

        val request = Request.Builder().url(httpUrl).build()

        val client = clientBuilder.addInterceptor(flakerInterceptor).build()

        val response = client.newCall(request).execute()

        assert(response.code == 200)
        assert(response.body?.string() == "Success")

        server.shutdown()
    }

    @Test
    fun `GIVEN shouldIntercept true, fail rate is 0 WHEN request intercepted THEN it should give server response`() {
        testFlakerPrefsProvider.setVersion(TestFlakerPrefsProvider.Version.FAKE_SUCCESS)

        flakerInterceptor = FlakerInterceptor.Builder(
            fakeContext,
            testNetworkRequestRepoProvider,
            testFlakerPrefsProvider
        )
            .failResponse(flakerFailResponse)
            .build()

        val server = MockWebServer()
        server.enqueue(MockResponse().setResponseCode(200).setBody("Success"))

        server.start()
        val httpUrl = server.url("/")

        val request = Request.Builder().url(httpUrl).build()

        val client = clientBuilder.addInterceptor(flakerInterceptor).build()

        val response = client.newCall(request).execute()

        assert(response.code == 200)
        assert(response.body?.string() == "Success")

        server.shutdown()
    }
}
