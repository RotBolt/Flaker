package io.github.rotbolt.flakerdata.flakerprefs

import io.github.rotbolt.flakedomain.prefs.FlakerPrefs
import kotlinx.coroutines.flow.Flow

interface PrefDataStore {

    fun getPrefs(): Flow<FlakerPrefs>

    suspend fun savePrefs(flakerPrefs: FlakerPrefs)
}
