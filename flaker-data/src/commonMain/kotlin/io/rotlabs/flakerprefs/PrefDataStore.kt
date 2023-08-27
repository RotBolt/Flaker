package io.rotlabs.flakerprefs

import io.rotlabs.flakedomain.prefs.FlakerPrefs
import kotlinx.coroutines.flow.Flow

interface PrefDataStore {

    fun getPrefs(): Flow<FlakerPrefs>

    suspend fun savePrefs(flakerPrefs: FlakerPrefs)
}
