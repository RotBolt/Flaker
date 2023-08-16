package io.rotlabs.flakerprefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.rotlabs.flakerprefs.dto.FlakerPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PrefDataStoreImpl(private val prefs: DataStore<Preferences>) : PrefDataStore {

    override fun getPrefs(): Flow<FlakerPrefs> {
        return prefs.data.map {
            FlakerPrefs(
                shouldIntercept = it[FLAKER_SHOULD_INTERCEPT] ?: false,
                delay = it[FLAKER_DELAY_VALUE] ?: 500,
                failPercent = it[FLAKER_FAIL_PERCENT] ?: 0,
                variancePercent = it[FLAKER_VARIANCE_PERCENT] ?: 0,
                retentionPolicy = when (it[FLAKER_RETENTION_POLICY]) {
                    RetentionPolicy.ONE_DAY.value -> RetentionPolicy.ONE_DAY
                    RetentionPolicy.SEVEN_DAYS.value -> RetentionPolicy.SEVEN_DAYS
                    RetentionPolicy.THIRTY_DAYS.value -> RetentionPolicy.FIFTEEN_DAYS
                    else -> RetentionPolicy.THIRTY_DAYS
                }
            )
        }
    }

    override suspend fun savePrefs(flakerPrefs: FlakerPrefs) {
        prefs.edit {
            it[FLAKER_SHOULD_INTERCEPT] = flakerPrefs.shouldIntercept
            it[FLAKER_DELAY_VALUE] = flakerPrefs.delay
            it[FLAKER_FAIL_PERCENT] = flakerPrefs.failPercent
            it[FLAKER_VARIANCE_PERCENT] = flakerPrefs.variancePercent
            it[FLAKER_RETENTION_POLICY] = flakerPrefs.retentionPolicy.value
        }
    }

    companion object {
        private val FLAKER_SHOULD_INTERCEPT = booleanPreferencesKey("flaker_should_intercept")
        private val FLAKER_DELAY_VALUE = intPreferencesKey("flaker_delay_value")
        private val FLAKER_FAIL_PERCENT = intPreferencesKey("flaker_fail_percent")
        private val FLAKER_VARIANCE_PERCENT = intPreferencesKey("flaker_variance_percent")
        private val FLAKER_RETENTION_POLICY = stringPreferencesKey("flaker_retention_policy")
    }
}
