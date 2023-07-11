package io.rotlabs.flakerretrofit.data

import android.content.Context
import io.rotlabs.flakedomain.networkrequest.NetworkRequest
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepo
import io.rotlabs.flakerdb.networkrequest.data.NetworkRequestRepoProvider
import io.rotlabs.flakerretrofit.FakeContext
import org.junit.Test

class FlakerRepoTest {

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
        val flakerRepo = FlakerRepo(fakeContext, testNetworkRequestRepoProvider, testFlakerPrefsProvider)

        val result = flakerRepo.allRequests()

        assert(result == fakeFlakerDataList)
    }

    @Test
    fun `GIVEN flakerPrefsProvider shouldIntercept WHEN flakerRepos isFlakerOn called THEN it should return true`() {
        val flakerRepo = FlakerRepo(fakeContext, testNetworkRequestRepoProvider, testFlakerPrefsProvider)

        val result = flakerRepo.isFlakerOn()

        assert(result)
    }
}
