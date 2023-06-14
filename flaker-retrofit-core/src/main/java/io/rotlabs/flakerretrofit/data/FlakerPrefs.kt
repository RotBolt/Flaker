package io.rotlabs.flakerretrofit.data

internal interface FlakerPrefs {
    fun shouldIntercept(): Boolean
    fun getDelay(): Long
    fun getFailPercent(): Int
    fun getVariancePercent(): Int
}