package io.rotlabs.flakerretrofit.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class TestFlakerPrefsProvider(context: Context) : FlakerPrefsProvider(context) {

    private val flakerPrefsFakeFail = object : FlakerPrefs {
        override fun getShouldInterceptFlow(): Flow<Boolean> = emptyFlow()

        override fun shouldIntercept(): Boolean = true

        override fun getDelay(): Long = 500L

        override fun getFailPercent(): Int = 100

        override fun getVariancePercent(): Int = 50

        override fun saveDelay(delay: Long) = Unit

        override fun saveFailPercent(failPercent: Int) = Unit

        override fun saveVariancePercent(variancePercent: Int) = Unit

        override fun saveShouldIntercept(shouldIntercept: Boolean) = Unit
    }

    private val flakerPrefsFakeSuccess = object : FlakerPrefs {

        override fun shouldIntercept(): Boolean = true

        override fun getShouldInterceptFlow(): Flow<Boolean> = flowOf(false, true)

        override fun getDelay(): Long = 500L

        override fun getFailPercent(): Int = 0

        override fun getVariancePercent(): Int = 50

        override fun saveDelay(delay: Long) = Unit

        override fun saveFailPercent(failPercent: Int) = Unit

        override fun saveVariancePercent(variancePercent: Int) = Unit

        override fun saveShouldIntercept(shouldIntercept: Boolean) = Unit
    }

    private val flakerPrefsFakeNoIntercept = object : FlakerPrefs {
        override fun shouldIntercept(): Boolean = false

        override fun getShouldInterceptFlow(): Flow<Boolean> = emptyFlow()

        override fun getDelay(): Long = 0L

        override fun getFailPercent(): Int = 0

        override fun getVariancePercent(): Int = 0

        override fun saveDelay(delay: Long) = Unit

        override fun saveFailPercent(failPercent: Int) = Unit

        override fun saveVariancePercent(variancePercent: Int) = Unit

        override fun saveShouldIntercept(shouldIntercept: Boolean) = Unit
    }

    private var version: Version? = null

    fun setVersion(version: Version) {
        this.version = version
    }

    override fun provide(): FlakerPrefs {
        return when (version ?: error("No version set")) {
            Version.FAKE_FAIL -> flakerPrefsFakeFail
            Version.FAKE_SUCCESS -> flakerPrefsFakeSuccess
            Version.FAKE_NO_INTERCEPT -> flakerPrefsFakeNoIntercept
        }
    }

    enum class Version {
        FAKE_FAIL, FAKE_SUCCESS, FAKE_NO_INTERCEPT
    }
}
