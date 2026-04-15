package com.asue24.gitlab

import android.content.Context
import androidx.datastore.core.DataStore
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.constants.GitlabRefreshToken
import com.asue24.gitlab.constants.Tokens
import net.openid.appauth.AuthorizationService

class AuthenticationRepository(
    val cntx: Context,
    dataStore: DataStore<GitlabRefreshToken>?,
) {
    private val accessToken = Tokens.accessToken
    public val authService = AuthorizationService(cntx.applicationContext)
    public val hasValidToken: Boolean = AuthStorage.getInstance(cntx) != null

}
