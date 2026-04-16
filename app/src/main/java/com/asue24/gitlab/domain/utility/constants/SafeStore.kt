package com.asue24.gitlab.domain.utility.constants

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import net.openid.appauth.AuthState

private val Context.dataStore by dataStore(
    fileName = "gitlab-refresh-token",
    serializer = RefreshTokenSerializer
)

val Context.authStateStore: DataStore<AuthState> by dataStore(
    fileName = "auth_state.pb",
    serializer = AuthStateSerializer
)

object AuthStorage {
    private var GlobalAuthState: DataStore<AuthState>? = null
    private var instance: DataStore<GitlabRefreshToken>? = null
    fun getInstance(context: Context): DataStore<GitlabRefreshToken> {
        if (instance == null) {
            instance = context.dataStore
        }
        return instance!!
    }
    fun getAuthState(context: Context): DataStore<AuthState> {
        if (GlobalAuthState == null) {
            GlobalAuthState = context.authStateStore
        }
        return GlobalAuthState!!
    }

}

