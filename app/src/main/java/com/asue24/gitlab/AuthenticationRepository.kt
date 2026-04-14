package com.asue24.gitlab

import android.content.Context
import androidx.datastore.core.DataStore
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.constants.GitlabRefreshToken
import com.asue24.gitlab.constants.Tokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

class AuthenticationRepository(val cntx: Context, dataStore: DataStore<GitlabRefreshToken>?) {
    private val accessToken = Tokens.accessToken
    public val authService = AuthorizationService(cntx.applicationContext)
    private val _authState = MutableStateFlow<AuthState?>(null)
    val authState: StateFlow<AuthState?> = _authState
    public val hasValidToken: Boolean = AuthStorage.getInstance(cntx) != null
}
