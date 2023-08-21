package io.rotlabs.flakerandroidui.screens.search

import androidx.compose.runtime.Immutable
import io.rotlabs.flakerandroidui.components.lists.NetworkRequestListContent

@Immutable
data class SearchUiDto(val filteredContent: NetworkRequestListContent?) {
    companion object {
        val IMMATERIAL = SearchUiDto(filteredContent = null)
    }
}
