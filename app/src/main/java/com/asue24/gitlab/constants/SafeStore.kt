package com.asue24.gitlab.constants

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

private val Context.dataStore by dataStore(
    fileName = "gitlab-refresh-token",
    serializer = RefreshTokenSerializer
)

object AuthStorage {
    private var instance: DataStore<GitlabRefreshToken>? = null
    fun getInstance(context: Context): DataStore<GitlabRefreshToken> {
        if (instance == null) {
            instance = context.dataStore
        }
        return instance!!
    }
}

