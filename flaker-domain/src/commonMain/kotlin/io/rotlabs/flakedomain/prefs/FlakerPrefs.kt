package io.rotlabs.flakedomain.prefs

data class FlakerPrefs(
    val shouldIntercept: Boolean,
    val delay: Int,
    val failPercent: Int,
    val variancePercent: Int,
    val retentionPolicy: RetentionPolicy
)
