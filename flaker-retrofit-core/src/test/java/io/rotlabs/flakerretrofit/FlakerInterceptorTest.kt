package io.rotlabs.flakerretrofit

import io.rotlabs.flakerprefs.dto.FlakerPrefs
import io.rotlabs.flakerretrofit.dto.FlakerFailResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlakerInterceptorTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val clientBuilder = OkHttpClient.Builder()

    private lateinit var flakerInterceptor: FlakerInterceptor

    private val flakerFailResponse = FlakerFailResponse()

    private val testDataDependencyContainer = TestDataDependencyContainer()

    @Before
    fun setup() {
        FlakerRetrofitDependencyContainer.init(testDataDependencyContainer)
    }

    @Test
    fun `GIVEN shouldIntercept true, fail rate 100 WHEN request intercepted THEN it should give fail response`() {
        runBlocking {
            testDataDependencyContainer.prefsDataStore.savePrefs(
                FlakerPrefs(
                    shouldIntercept = true,
                    delay = 0,
                    failPercent = 100,
                    variancePercent = 0
                )
            )

            flakerInterceptor = FlakerInterceptor.Builder()
                .failResponse(flakerFailResponse)
                .build()

            val request = Request.Builder().url("https://localhost:8080/").build()

            val client = clientBuilder.addInterceptor(flakerInterceptor).build()

            val response = client.newCall(request).execute()
            assert(response.code == flakerFailResponse.httpCode)
            assert(response.message == flakerFailResponse.message)
        }
    }

    @Test
    fun `GIVEN flakerPrefs shouldIntercept false WHEN request intercepted THEN it should give server response`() {
        runBlocking {
            testDataDependencyContainer.prefsDataStore.savePrefs(
                FlakerPrefs(
                    shouldIntercept = false,
                    delay = 0,
                    failPercent = 0,
                    variancePercent = 0
                )
            )

            flakerInterceptor = FlakerInterceptor.Builder()
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

    @Test
    fun `GIVEN shouldIntercept true, fail rate is 0 WHEN request intercepted THEN it should give server response`() {
        runBlocking {
            testDataDependencyContainer.prefsDataStore.savePrefs(
                FlakerPrefs(
                    shouldIntercept = true,
                    delay = 0,
                    failPercent = 0,
                    variancePercent = 0
                )
            )

            flakerInterceptor = FlakerInterceptor.Builder()
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
}
