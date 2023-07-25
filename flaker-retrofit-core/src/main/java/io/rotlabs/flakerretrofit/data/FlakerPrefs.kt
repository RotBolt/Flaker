package io.rotlabs.flakerretrofit.data

import kotlinx.coroutines.flow.Flow

interface FlakerPrefs {

    fun getShouldInterceptFlow(): Flow<Boolean>

    fun shouldIntercept(): Boolean
    fun getDelay(): Long
    fun getFailPercent(): Int
    fun getVariancePercent(): Int

    fun saveDelay(delay: Long)
    fun saveFailPercent(failPercent: Int)
    fun saveVariancePercent(variancePercent: Int)
    fun saveShouldIntercept(shouldIntercept: Boolean)
}
