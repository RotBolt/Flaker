package io.rotlabs.flakerandroidui.screens.prefs

data class FlakerPrefsUiDto(
    val delay: Int,
    val failPercent: Int,
    val variancePercent: Int,
    val retentionPolicyDays: String,
) {
    companion object {
        val IMMATERIAL = FlakerPrefsUiDto(
            delay = -1,
            failPercent = -1,
            variancePercent = -1,
            retentionPolicyDays = ""
        )
    }
}
