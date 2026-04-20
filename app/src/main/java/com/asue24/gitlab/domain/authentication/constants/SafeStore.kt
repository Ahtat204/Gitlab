package com.asue24.gitlab.domain.authentication.constants

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import net.openid.appauth.AuthState

val Context.authStateStore: DataStore<AuthState> by dataStore(
    fileName = "auth_state.pb",
    serializer = AuthStateSerializer
)

object AuthStorage {
    private var GlobalAuthState: DataStore<AuthState>? = null
    fun getAuthState(context: Context): DataStore<AuthState> {
        if (GlobalAuthState == null) {
            GlobalAuthState = context.authStateStore
        }
        return GlobalAuthState!!
    }

}

