package io.rotlabs.flakerretrofit.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.rotlabs.flakerretrofit.domain.FLAKER_SHARED_PREFS
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class FlakerPrefsImpl(context: Context) : FlakerPrefs {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(FLAKER_SHARED_PREFS, Context.MODE_PRIVATE)

    override fun getShouldInterceptFlow(): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == FLAKER_PREF_SHOULD_INTERCEPT) {
                trySend(shouldIntercept())
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(shouldIntercept())
        awaitClose { sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun shouldIntercept(): Boolean {
        return sharedPrefs.getBoolean(FLAKER_PREF_SHOULD_INTERCEPT, false)
    }

    override fun getDelay(): Long {
        return sharedPrefs.getLong(FLAKER_PREF_DELAY, 0L)
    }

    override fun getFailPercent(): Int {
        return sharedPrefs.getInt(FLAKER_PREF_FAIL_PERCENT, 0)
    }

    override fun getVariancePercent(): Int {
        return sharedPrefs.getInt(FLAKER_PREF_VARIANCE_PERCENT, 0)
    }

    override fun saveDelay(delay: Long) {
        sharedPrefs.edit { putLong(FLAKER_PREF_DELAY, delay) }
    }

    override fun saveFailPercent(failPercent: Int) {
        sharedPrefs.edit { putInt(FLAKER_PREF_FAIL_PERCENT, failPercent) }
    }

    override fun saveVariancePercent(variancePercent: Int) {
        sharedPrefs.edit { putInt(FLAKER_PREF_VARIANCE_PERCENT, variancePercent) }
    }

    override fun saveShouldIntercept(shouldIntercept: Boolean) {
        sharedPrefs.edit { putBoolean(FLAKER_PREF_SHOULD_INTERCEPT, shouldIntercept) }
    }
}
