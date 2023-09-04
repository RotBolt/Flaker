package io.github.rotbolt.flakerokhttpcore

import io.github.rotbolt.flakedomain.prefs.FlakerPrefs
import io.github.rotbolt.flakedomain.prefs.RetentionPolicy
import io.github.rotbolt.flakerokhttpcore.di.FlakerOkHttpCoreContainer
import io.github.rotbolt.flakerokhttpcore.dto.FlakerFailResponse
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

    private val testDataDependencyContainer = TestDataContainer()

    private val testMonitorContainer = TestMonitorContainer()

    @Before
    fun setup() {
        FlakerOkHttpCoreContainer.install(testDataDependencyContainer, testMonitorContainer)
    }

    @Test
    fun `GIVEN shouldIntercept true, fail rate 100 WHEN request intercepted THEN it should give fail response`() {
        runBlocking {
            testDataDependencyContainer.prefsDataStore.savePrefs(
                FlakerPrefs(
                    shouldIntercept = true,
                    delay = 0,
                    failPercent = 100,
                    variancePercent = 0,
                    retentionPolicy = RetentionPolicy.THIRTY_DAYS
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
                    variancePercent = 0,
                    retentionPolicy = RetentionPolicy.THIRTY_DAYS
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
                    variancePercent = 0,
                    retentionPolicy = RetentionPolicy.THIRTY_DAYS
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
