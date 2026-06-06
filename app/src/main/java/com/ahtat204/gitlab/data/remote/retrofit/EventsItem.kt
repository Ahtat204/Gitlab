package com.ahtat204.gitlab.data.remote.retrofit

data class EventsItem(
    val action_name: String,
    val author_username: String,
    val created_at: String,
    val project_id: Int,
    val target_title: String,//keep
    val target_type: String //keep
)