package io.rotlabs.flakerretrofit.data

interface FlakerPrefs {
    fun shouldIntercept(): Boolean
    fun getDelay(): Long
    fun getFailPercent(): Int
    fun getVariancePercent(): Int
}
