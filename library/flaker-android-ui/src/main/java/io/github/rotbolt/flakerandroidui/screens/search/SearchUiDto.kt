package io.github.rotbolt.flakerandroidui.screens.search

import androidx.compose.runtime.Immutable
import io.github.rotbolt.flakerandroidui.components.lists.NetworkRequestListContent

@Immutable
data class SearchUiDto(val filteredContent: NetworkRequestListContent?) {
    companion object {
        val IMMATERIAL = SearchUiDto(filteredContent = null)
    }
}
