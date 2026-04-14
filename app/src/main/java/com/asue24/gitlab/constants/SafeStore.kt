package com.asue24.gitlab.constants

import android.content.Context
import androidx.datastore.dataStore

object SafeStore
{
    val Context.datastore by dataStore(
        fileName = "gitlab-refresh-token", serializer = RefreshTokenSerializer
    )
}