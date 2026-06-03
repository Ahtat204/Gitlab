package com.ahtat204.gitlab.data.remote.retrofit

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PushData(
    val action: String,
    val commit_count: Int,
    val commit_from: String,
    val commit_title: String,
     val commit_to: String,
    @Transient val ref: String="",
    @Transient val ref_count: Any="",
    val ref_type: String
)