package com.asue24.gitlab.data.repositories

import android.content.Context
import com.asue24.gitlab.domain.utility.constants.Tokens
import net.openid.appauth.AuthorizationService

class AuthenticationRepository(
    val cntx: Context,
) {
    private val accessToken = Tokens.accessToken
    val authService = AuthorizationService(cntx.applicationContext)
    private var authenticationService: AuthorizationService? = null

}
