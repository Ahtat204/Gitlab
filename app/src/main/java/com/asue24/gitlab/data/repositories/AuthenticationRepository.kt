package com.asue24.gitlab.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import com.asue24.gitlab.domain.utility.constants.GitlabRefreshToken
import com.asue24.gitlab.domain.utility.constants.Tokens
import net.openid.appauth.AuthorizationService

class AuthenticationRepository(
    val cntx: Context,
    dataStore: DataStore<GitlabRefreshToken>?,
) {
    private val accessToken = Tokens.accessToken
    val authService = AuthorizationService(cntx.applicationContext)
    val hasValidToken: Boolean = AuthStorage.getInstance(cntx) != null

}
