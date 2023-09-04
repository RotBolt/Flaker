package io.github.rotbolt.flakerandroidui.components.lists

import io.github.rotbolt.flakedomain.networkrequest.NetworkRequest

sealed class NetworkRequestUi {
    data class DateItem(val formattedString: String) : NetworkRequestUi()
    data class NetworkRequestItem(val networkRequest: NetworkRequest) : NetworkRequestUi()
}
