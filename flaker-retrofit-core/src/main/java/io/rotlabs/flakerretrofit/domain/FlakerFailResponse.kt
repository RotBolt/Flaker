package io.rotlabs.flakerretrofit.domain

data class FlakerFailResponse(
    val httpCode: Int = 500,
    val message: String = FLAKER_FAIL_RESPONSE_STRING,
    val responseBodyString: String = FLAKER_FAIL_RESPONSE_STRING
)
