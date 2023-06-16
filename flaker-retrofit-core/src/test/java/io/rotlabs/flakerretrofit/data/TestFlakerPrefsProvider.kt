package io.rotlabs.flakerretrofit.data

import android.content.Context

class TestFlakerPrefsProvider(context: Context) : FlakerPrefsProvider(context) {

    private val flakerPrefsFakeFail = object : FlakerPrefs {
        override fun shouldIntercept(): Boolean = true

        override fun getDelay(): Long = 500L

        override fun getFailPercent(): Int = 100

        override fun getVariancePercent(): Int = 50
    }

    private val flakerPrefsFakeSuccess = object : FlakerPrefs {
        override fun shouldIntercept(): Boolean = true

        override fun getDelay(): Long = 500L

        override fun getFailPercent(): Int = 0

        override fun getVariancePercent(): Int = 50
    }

    private val flakerPrefsFakeNoIntercept = object : FlakerPrefs {
        override fun shouldIntercept(): Boolean = false

        override fun getDelay(): Long = 0L

        override fun getFailPercent(): Int = 0

        override fun getVariancePercent(): Int = 0
    }

    private var version: Version? = null

    fun setVersion(version: Version) {
        this.version = version
    }

    override fun provide(): FlakerPrefs {
        val versionToUse = version ?: error("No version set")
        return when (versionToUse) {
            Version.FAKE_FAIL -> flakerPrefsFakeFail
            Version.FAKE_SUCCESS -> flakerPrefsFakeSuccess
            Version.FAKE_NO_INTERCEPT -> flakerPrefsFakeNoIntercept
        }
    }

    enum class Version {
        FAKE_FAIL, FAKE_SUCCESS, FAKE_NO_INTERCEPT
    }
}
