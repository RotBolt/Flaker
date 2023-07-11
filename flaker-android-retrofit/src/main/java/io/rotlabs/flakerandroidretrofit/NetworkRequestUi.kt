package io.rotlabs.flakerandroidretrofit

import io.rotlabs.flakedomain.networkrequest.NetworkRequest

sealed class NetworkRequestUi {
    data class DateItem(val formattedString: String) : NetworkRequestUi()
    data class NetworkRequestItem(val networkRequest: NetworkRequest) : NetworkRequestUi()
}
