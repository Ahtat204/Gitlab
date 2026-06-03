package com.ahtat204.gitlab.data.remote.retrofit

data class Note(
    val author: Author,
    val body: String,
    val commands_changes: CommandsChanges,
    val confidential: Boolean,
    val created_at: String,
    val id: Long,
    val imported: Boolean,
    val imported_from: String,
    val `internal`: Boolean,
    val noteable_id: Int,
    val noteable_iid: Int,
    val noteable_type: String,
    val project_id: Int,
    val resolvable: Boolean,
    val system: Boolean,
    val type: Any,
    val updated_at: String
)