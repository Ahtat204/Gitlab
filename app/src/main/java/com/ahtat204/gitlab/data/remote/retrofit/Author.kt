package com.ahtat204.gitlab.data.remote.retrofit

data class Author(
    val avatar_url: String,
    val id: Int,
    val locked: Boolean,
    val name: String,
    val public_email: String,
    val state: String,
    val username: String,
    val web_url: String
)