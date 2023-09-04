package io.github.rotbolt.flakerandroidokhttp.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.github.rotbolt.flakedomain.networkrequest.NetworkRequest
import io.github.rotbolt.flakedomain.prefs.FlakerPrefs
import io.github.rotbolt.flakedomain.prefs.RetentionPolicy
import io.github.rotbolt.flakerandroidmonitor.FlakerMonitor
import io.github.rotbolt.flakerandroidokhttp.MainDispatcherRule
import io.github.rotbolt.flakerandroidui.screens.prefs.FlakerPrefsUiDto
import io.github.rotbolt.flakerandroidui.screens.search.SearchUiDto
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.InMemoryNetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerdb.networkrequest.NetworkRequestRepo
import io.github.rotbolt.flakerdata.flakerprefs.PrefDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlakerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testFlakerMonitor = object : FlakerMonitor {
        override fun initialize(appContext: Context) = Unit

        override fun captureException(throwable: Throwable, data: Map<String, Any?>?) = Unit
    }

    private val testPrefDataStore = object : PrefDataStore {
        private val prefFlow = MutableStateFlow(
            FlakerPrefs(
                shouldIntercept = false,
                delay = 0,
                failPercent = 0,
                variancePercent = 0,
                retentionPolicy = RetentionPolicy.THIRTY_DAYS
            )
        )

        override fun getPrefs(): Flow<FlakerPrefs> = flow {
            emit(prefFlow.value)
            emitAll(prefFlow)
        }

        override suspend fun savePrefs(flakerPrefs: FlakerPrefs) {
            prefFlow.emit(flakerPrefs)
        }
    }

    private val testNetworkRequestRepo: NetworkRequestRepo = InMemoryNetworkRequestRepo(UnconfinedTestDispatcher())

    private val savedStateHandle = SavedStateHandle()

    private lateinit var flakerViewModel: FlakerViewModel

    @Before
    fun setup() = runBlocking {
        testPrefDataStore.savePrefs(
            FlakerPrefs(
                shouldIntercept = true,
                delay = 500,
                failPercent = 50,
                variancePercent = 0,
                retentionPolicy = RetentionPolicy.FIFTEEN_DAYS
            )
        )
        testNetworkRepoList().forEach {
            testNetworkRequestRepo.insert(it)
        }
        flakerViewModel = FlakerViewModel(
            testNetworkRequestRepo,
            testPrefDataStore,
            testFlakerMonitor,
            savedStateHandle
        )
    }

    @Test
    fun `test init`() = runBlocking {
        flakerViewModel.viewStateFlow.test {
            val item = awaitItem()
            assertTrue(item.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item.searchData)
            assertEquals(3, item.networkRequests.size)
            assertFalse(item.toShowSearch)
        }
    }

    @Test
    fun `test toggleFlaker`() = runBlocking {
        flakerViewModel.viewStateFlow.test {
            val item = awaitItem()
            assertTrue(item.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item.searchData)
            assertEquals(3, item.networkRequests.size)
            assertFalse(item.toShowSearch)

            flakerViewModel.toggleFlaker(false)

            val item1 = awaitItem()
            assertFalse(item1.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item1.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item1.searchData)
            assertEquals(3, item1.networkRequests.size)
            assertFalse(item1.toShowSearch)
        }
    }

    @Test
    fun `test updatePrefs`() = runBlocking {
        flakerViewModel.viewStateFlow.test {
            val item = awaitItem()
            assertTrue(item.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item.searchData)
            assertEquals(3, item.networkRequests.size)
            assertFalse(item.toShowSearch)

            flakerViewModel.openPrefs()

            val expectedPrefsUiDto = FlakerPrefsUiDto(
                delay = 500,
                failPercent = 50,
                variancePercent = 0,
                retentionPolicyDays = RetentionPolicy.FIFTEEN_DAYS.value
            )
            val item1 = awaitItem()
            assertTrue(item1.isFlakerOn)
            assertEquals(expectedPrefsUiDto, item1.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item1.searchData)
            assertEquals(3, item1.networkRequests.size)
            assertFalse(item1.toShowSearch)

            flakerViewModel.updatePrefs(
                FlakerPrefsUiDto(
                    delay = 1000,
                    failPercent = 100,
                    variancePercent = 0,
                    retentionPolicyDays = RetentionPolicy.THIRTY_DAYS.value
                )
            )

            val item2 = awaitItem()
            assertTrue(item2.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item2.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item2.searchData)
            assertEquals(3, item2.networkRequests.size)
            assertFalse(item2.toShowSearch)
        }
    }

    @Test
    fun `test search`() = runBlocking {
        flakerViewModel.viewStateFlow.test {
            val item = awaitItem()
            assertTrue(item.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item.searchData)
            assertEquals(3, item.networkRequests.size)
            assertFalse(item.toShowSearch)

            flakerViewModel.openSearch()

            val item1 = awaitItem()
            assertTrue(item1.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item1.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item1.searchData)
            assertEquals(3, item1.networkRequests.size)
            assertTrue(item1.toShowSearch)

            flakerViewModel.searchNetworkRequests("todos/2")

            val item2 = awaitItem()
            assertTrue(item2.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item2.currentPrefs)
            assertEquals(1, item2.searchData.filteredContent?.size)
            assertEquals(3, item2.networkRequests.size)
            assertTrue(item2.toShowSearch)

            flakerViewModel.closeSearch()

            val item3 = awaitItem()
            assertTrue(item3.isFlakerOn)
            assertEquals(FlakerPrefsUiDto.IMMATERIAL, item3.currentPrefs)
            assertEquals(SearchUiDto.IMMATERIAL, item3.searchData)
            assertEquals(3, item3.networkRequests.size)
            assertFalse(item3.toShowSearch)
        }
    }

    private fun testNetworkRepoList(): List<NetworkRequest> {
        val now = System.currentTimeMillis()
        val millisInDay = 24 * 60 * 60 * 1000L
        return listOf(
            NetworkRequest(
                host = "https://jsonplaceholder.typicode.com",
                path = "/todos/1",
                method = "GET",
                requestTime = now,
                responseCode = 500,
                responseTimeTaken = 100,
                isFailedByFlaker = true,
                createdAt = now
            ),
            NetworkRequest(
                host = "https://jsonplaceholder.typicode.com",
                path = "/todos/1",
                method = "GET",
                requestTime = now - (2 * millisInDay),
                responseCode = 200,
                responseTimeTaken = 100,
                isFailedByFlaker = false,
                createdAt = now - (2 * millisInDay)
            ),
            NetworkRequest(
                host = "https://jsonplaceholder.typicode.com",
                path = "/todos/2",
                method = "GET",
                requestTime = now - (8 * millisInDay),
                responseCode = 200,
                responseTimeTaken = 100,
                isFailedByFlaker = false,
                createdAt = now - (8 * millisInDay)
            ),
            NetworkRequest(
                host = "https://jsonplaceholder.typicode.com",
                path = "/todos/2",
                method = "GET",
                requestTime = now - (16 * millisInDay),
                responseCode = 500,
                responseTimeTaken = 100,
                isFailedByFlaker = true,
                createdAt = now - (16 * millisInDay)
            ),
            NetworkRequest(
                host = "https://jsonplaceholder.typicode.com",
                path = "/todos/1",
                method = "GET",
                requestTime = now - (31 * millisInDay),
                responseCode = 200,
                responseTimeTaken = 100,
                isFailedByFlaker = false,
                createdAt = now - (31 * millisInDay)
            )
        )
    }
}
