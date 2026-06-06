package com.ahtat204.gitlab.data.remote.retrofit

import kotlinx.serialization.Serializable

@Serializable
data class Author(
    val id: Int, val name: String, val username: String
)