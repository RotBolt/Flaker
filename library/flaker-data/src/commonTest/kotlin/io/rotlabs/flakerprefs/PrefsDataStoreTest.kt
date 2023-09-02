package io.rotlabs.flakerprefs

import io.rotlabs.flakedomain.prefs.FlakerPrefs
import io.rotlabs.flakedomain.prefs.RetentionPolicy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PrefsDataStoreTest {

    private lateinit var prefDataStore: PrefDataStore

    @BeforeTest
    fun setup() {
        val dataStore = testDataStore()
        prefDataStore = PrefDataStoreImpl(dataStore)
    }

    @Test
    fun `test save and get prefs`() = runBlocking {
        prefDataStore.savePrefs(
            FlakerPrefs(
                shouldIntercept = true,
                delay = 1000,
                failPercent = 10,
                variancePercent = 10,
                retentionPolicy = RetentionPolicy.ONE_DAY
            )
        )
        val prefs = prefDataStore.getPrefs().first()
        assertTrue(prefs.shouldIntercept)
        assertEquals(1000, prefs.delay)
        assertEquals(10, prefs.failPercent)
        assertEquals(10, prefs.variancePercent)
        assertEquals(prefs.retentionPolicy, RetentionPolicy.ONE_DAY)
    }
}
