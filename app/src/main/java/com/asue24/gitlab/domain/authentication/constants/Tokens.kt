package com.asue24.gitlab.domain.authentication.constants

import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

object Tokens {
    var accessToken: String? = null
    var CurrentAuthState:AuthState?=null
    var authService:AuthorizationService?=null
}

