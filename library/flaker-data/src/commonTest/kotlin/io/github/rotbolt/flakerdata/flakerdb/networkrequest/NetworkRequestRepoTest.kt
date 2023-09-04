package io.github.rotbolt.flakerdata.flakerdb.networkrequest

import app.cash.turbine.test
import io.github.rotbolt.flakedomain.networkrequest.NetworkRequest
import io.github.rotbolt.flakedomain.prefs.RetentionPolicy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.datetime.Clock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkRequestRepoTest {

    private lateinit var networkRequestRepo: NetworkRequestRepo

    private suspend fun NetworkRequestRepo.seed() {
        val now = Clock.System.now().toEpochMilliseconds()
        insert(
            NetworkRequest(
                host = "https://jsonplaceholder.typicode.com",
                path = "/todos/1",
                method = "GET",
                requestTime = now,
                responseCode = 200,
                responseTimeTaken = 100,
                isFailedByFlaker = false,
                createdAt = now
            )
        )
    }

    @BeforeTest
    fun setup() = runBlocking {
        networkRequestRepo = InMemoryNetworkRequestRepo(UnconfinedTestDispatcher())
        networkRequestRepo.deleteAll()
        networkRequestRepo.seed()
    }

    @Test
    fun `test selectAll`() = runBlocking {
        val networkRequests = networkRequestRepo.selectAll()
        assertTrue(networkRequests.isNotEmpty())
        assertEquals(1, networkRequests.size)
    }

    @Test
    fun `test observeAll`() = runBlocking {
        val networkRequests = networkRequestRepo.observeAll()
        networkRequests.test {
            assertEquals(1, awaitItem().size)
            networkRequestRepo.insert(
                NetworkRequest(
                    host = "https://jsonplaceholder.typicode.com",
                    path = "/todos/1",
                    method = "GET",
                    requestTime = 1693127126000,
                    responseCode = 500,
                    responseTimeTaken = 100,
                    isFailedByFlaker = true,
                    createdAt = 1693127139000
                )
            )
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals(500, items[1].responseCode)
            assertEquals(true, items[1].isFailedByFlaker)
        }
    }

    @Test
    fun `test deleteAll`() = runBlocking {
        assertEquals(1, networkRequestRepo.selectAll().size)
        networkRequestRepo.deleteAll()
        assertEquals(0, networkRequestRepo.selectAll().size)
    }

    @Test
    fun `test deleteExpiredData`() = runBlocking {
        val millisInOneDay = 86400000L
        val now = Clock.System.now().toEpochMilliseconds()
        // insert 2 days before time
        val networkRequest0 = NetworkRequest(
            host = "https://jsonplaceholder.typicode.com",
            path = "/todos/1",
            method = "GET",
            requestTime = now - (millisInOneDay * 2),
            responseCode = 200,
            responseTimeTaken = 100,
            isFailedByFlaker = false,
            createdAt = now - (millisInOneDay * 2)
        )
        networkRequestRepo.insert(networkRequest0)
        assertEquals(2, networkRequestRepo.selectAll().size)
        networkRequestRepo.deleteExpiredData(RetentionPolicy.ONE_DAY)
        assertEquals(1, networkRequestRepo.selectAll().size)

        // insert 7 days before time
        val networkRequest1 = NetworkRequest(
            host = "https://jsonplaceholder.typicode.com",
            path = "/todos/1",
            method = "GET",
            requestTime = now - (millisInOneDay * 8),
            responseCode = 200,
            responseTimeTaken = 100,
            isFailedByFlaker = false,
            createdAt = now - (millisInOneDay * 8)
        )
        networkRequestRepo.insert(networkRequest1)
        assertEquals(2, networkRequestRepo.selectAll().size)
        networkRequestRepo.deleteExpiredData(RetentionPolicy.SEVEN_DAYS)
        assertEquals(1, networkRequestRepo.selectAll().size)

        // insert 15 days before time
        val networkRequest2 = NetworkRequest(
            host = "https://jsonplaceholder.typicode.com",
            path = "/todos/1",
            method = "GET",
            requestTime = now - (millisInOneDay * 16),
            responseCode = 200,
            responseTimeTaken = 100,
            isFailedByFlaker = false,
            createdAt = now - (millisInOneDay * 16)
        )
        networkRequestRepo.insert(networkRequest2)
        assertEquals(2, networkRequestRepo.selectAll().size)
        networkRequestRepo.deleteExpiredData(RetentionPolicy.FIFTEEN_DAYS)
        assertEquals(1, networkRequestRepo.selectAll().size)

        // insert 30 days before time
        val networkRequest3 = NetworkRequest(
            host = "https://jsonplaceholder.typicode.com",
            path = "/todos/1",
            method = "GET",
            requestTime = now - (millisInOneDay * 31),
            responseCode = 200,
            responseTimeTaken = 100,
            isFailedByFlaker = false,
            createdAt = now - (millisInOneDay * 31)
        )
        networkRequestRepo.insert(networkRequest3)
        assertEquals(2, networkRequestRepo.selectAll().size)
        networkRequestRepo.deleteExpiredData(RetentionPolicy.THIRTY_DAYS)
        assertEquals(1, networkRequestRepo.selectAll().size)
    }
}