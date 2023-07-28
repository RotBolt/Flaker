package io.rotlabs.flakerprefs.dto

data class FlakerPrefs(
    val shouldIntercept: Boolean,
    val delay: Int,
    val failPercent: Int,
    val variancePercent: Int
)
