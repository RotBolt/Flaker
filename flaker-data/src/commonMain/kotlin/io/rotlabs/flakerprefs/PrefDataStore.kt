package io.rotlabs.flakerprefs

import io.rotlabs.flakerprefs.dto.FlakerPrefs
import kotlinx.coroutines.flow.Flow

interface PrefDataStore {

    fun getPrefs(): Flow<FlakerPrefs>

    suspend fun savePrefs(flakerPrefs: FlakerPrefs)
}
