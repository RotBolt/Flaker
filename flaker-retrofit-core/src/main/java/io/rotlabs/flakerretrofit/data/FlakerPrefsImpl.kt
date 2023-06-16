package io.rotlabs.flakerretrofit.data

import android.content.Context
import android.content.SharedPreferences
import io.rotlabs.flakerretrofit.domain.FLAKER_SHARED_PREFS

internal class FlakerPrefsImpl(context: Context) : FlakerPrefs {

    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(FLAKER_SHARED_PREFS, Context.MODE_PRIVATE)

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
}
