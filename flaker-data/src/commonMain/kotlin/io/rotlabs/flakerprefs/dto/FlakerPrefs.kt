package io.rotlabs.flakerprefs.dto

import io.rotlabs.flakerprefs.RetentionPolicy

data class FlakerPrefs(
    val shouldIntercept: Boolean,
    val delay: Int,
    val failPercent: Int,
    val variancePercent: Int,
    val retentionPolicy: RetentionPolicy
)
