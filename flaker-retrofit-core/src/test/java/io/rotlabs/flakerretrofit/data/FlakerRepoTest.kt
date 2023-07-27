package io.rotlabs.flakerretrofit.data

import android.content.Context
import app.cash.turbine.test
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoProvider
import io.rotlabs.flakerretrofit.FakeContext
import io.rotlabs.flakerretrofit.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlakerRepoTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeContext: Context = FakeContext()

    private val fakeFlakerDataList = mutableListOf<NetworkRequest>().apply {
        val request0 = NetworkRequest(
            host = "localhost:8080",
            path = "/v1/sample",
            method = "GET",
            requestTime = 1687679190000,
            responseCode = 200,
            responseTimeTaken = 150,
            isFailedByFlaker = false
        )
        val request1 = NetworkRequest(
            host = "localhost:8080",
            path = "/v1/sample",
            method = "POST",
            requestTime = 1687679190000,
            responseCode = 500,
            responseTimeTaken = 2000,
            isFailedByFlaker = true
        )
        add(request0)
        add(request1)
    }.toList()

    private val testNetworkRequestRepoProvider = object : NetworkRequestRepoProvider(fakeContext) {

        val fakeDataRepo = object : NetworkRequestRepo {
            override fun selectAll(): List<NetworkRequest> {
                return fakeFlakerDataList
            }

            override fun insert(networkRequest: NetworkRequest) = Unit

            override fun observeAll(): Flow<List<NetworkRequest>> {
                return flowOf(fakeFlakerDataList, fakeFlakerDataList.subList(0, fakeFlakerDataList.size - 1))
            }
        }

        override fun provide(): NetworkRequestRepo {
            return fakeDataRepo
        }
    }

    private val testFlakerPrefsProvider: FlakerPrefsProvider = TestFlakerPrefsProvider(fakeContext).apply {
        setVersion(TestFlakerPrefsProvider.Version.FAKE_SUCCESS)
    }

    @Test
    fun `GIVEN networkRequestRepo has data WHEN flakerRepos allRequests() called THEN it should return same data`() {
        runBlocking {
            val flakerRepo =
                FlakerRepo(
                    fakeContext,
                    testNetworkRequestRepoProvider,
                    testFlakerPrefsProvider,
                    mainDispatcherRule.testDispatcher
                )

            val result = flakerRepo.allRequests()

            assert(result == fakeFlakerDataList)
        }
    }

    @Test
    fun `GIVEN flakerPrefsProvider shouldIntercept WHEN flakerRepos isFlakerOn called THEN it should return true`() {
        runBlocking {
            val flakerRepo =
                FlakerRepo(
                    fakeContext,
                    testNetworkRequestRepoProvider,
                    testFlakerPrefsProvider,
                    mainDispatcherRule.testDispatcher
                )

            val result = flakerRepo.isFlakerOn()

            assert(result)
        }
    }

    @Test
    fun `GIVEN networkRequestRepo has data WHEN observeAllRequests() called THEN it should return same data`() {
        runBlocking {
            val flakerRepo =
                FlakerRepo(
                    fakeContext,
                    testNetworkRequestRepoProvider,
                    testFlakerPrefsProvider,
                    mainDispatcherRule.testDispatcher
                )

            flakerRepo.observeAllRequests().test {
                assert(awaitItem() == fakeFlakerDataList)
                assert(awaitItem() == fakeFlakerDataList.subList(0, fakeFlakerDataList.size - 1))
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `GIVEN shouldIntercept values WHEN flakerRepos observeFlakerOn called THEN it should observe values`() {
        runBlocking {
            val flakerRepo =
                FlakerRepo(
                    fakeContext,
                    testNetworkRequestRepoProvider,
                    testFlakerPrefsProvider,
                    mainDispatcherRule.testDispatcher
                )

            flakerRepo.observeFlakerOn().test {
                assert(awaitItem().not())
                assert(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
