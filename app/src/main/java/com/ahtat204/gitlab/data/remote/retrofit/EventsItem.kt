package com.ahtat204.gitlab.data.remote.retrofit

import kotlinx.serialization.Serializable

@Serializable
data class EventsItem(
    val action_name: String,
    val author: Author,
    val author_id: Int,
    val author_username: String,
    val created_at: String,
    val imported: Boolean,
    val project_id: Int,
    val push_data: PushData,
    val target_title: String,//keep
    val target_type: String //keep
)