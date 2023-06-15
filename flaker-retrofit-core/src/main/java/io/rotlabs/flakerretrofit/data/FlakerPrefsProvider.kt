package io.rotlabs.flakerretrofit.data

import android.content.Context

open class FlakerPrefsProvider (private val context: Context) {
    open fun provide(): FlakerPrefs = FlakerPrefsImpl(context)
}