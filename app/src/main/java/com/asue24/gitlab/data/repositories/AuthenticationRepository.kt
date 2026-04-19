package com.asue24.gitlab.data.repositories

import android.content.Context
import net.openid.appauth.AuthorizationService

class AuthenticationRepository(
    val cntx: Context,
) {
    val authService = AuthorizationService(cntx.applicationContext)
    private var authenticationService: AuthorizationService? = null

}
