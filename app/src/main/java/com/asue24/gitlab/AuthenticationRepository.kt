package com.asue24.gitlab

import android.content.Context
import androidx.datastore.core.DataStore
import com.asue24.gitlab.constants.GitlabRefreshToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

class AuthenticationRepository(context: Context, dataStore: DataStore<GitlabRefreshToken>?) {
    public val authService = AuthorizationService(context.applicationContext)
    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState: StateFlow<AuthState?> = _authState
}