package io.rotlabs.flakerretrofit

import android.content.Context
import android.content.SharedPreferences

class FlakerPrefs private constructor(private val sharedPrefs: SharedPreferences) {

    fun shouldIntercept(): Boolean {
        return sharedPrefs.getBoolean(FLAKER_PREF_SHOULD_INTERCEPT, false)
    }

    fun getDelay(): Long {
        return sharedPrefs.getLong(FLAKER_PREF_DELAY, 0L)
    }

    fun getFailPercent(): Int {
        return sharedPrefs.getInt(FLAKER_PREF_FAIL_PERCENT, 0)
    }

    fun getVariancePercent(): Int {
        return sharedPrefs.getInt(FLAKER_PREF_VARIANCE_PERCENT, 0)
    }

    companion object {
        fun instance(context: Context): FlakerPrefs {
            val sharedPrefs = context.getSharedPreferences(FLAKER_SHARED_PREFS, Context.MODE_PRIVATE)
            return FlakerPrefs(sharedPrefs)
        }
    }
}