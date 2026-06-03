package com.ahtat204.gitlab.data.remote.retrofit

data class PushData(
    val action: String,
    val commit_count: Int,
    val commit_from: String,
    val commit_title: String,
    val commit_to: String,
    val ref: String,
    val ref_count: Any,
    val ref_type: String
)