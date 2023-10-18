package io.github.bolt.flakersampleapp.home.data.remote.dto

@Suppress("ConstructorParameterNaming")
data class UserData(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<User>,
    val support: Support
)

@Suppress("ConstructorParameterNaming")
data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

data class Support(
    val url: String,
    val text: String
)
