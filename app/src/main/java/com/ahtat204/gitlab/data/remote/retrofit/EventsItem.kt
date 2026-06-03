package com.ahtat204.gitlab.data.remote.retrofit

data class EventsItem(
    val action_name: String,
    val author: Author,
    val author_id: Int,
    val author_username: String,
    val created_at: String,
    val id: Long,
    val imported: Boolean,
    val imported_from: String,
    val note: Note,
    val project_id: Int,
    val push_data: PushData,
    val target_id: Long,
    val target_iid: Long,
    val target_title: String,
    val target_type: String
)