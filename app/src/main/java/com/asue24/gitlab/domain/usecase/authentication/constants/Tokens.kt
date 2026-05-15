package com.asue24.gitlab.domain.usecase.authentication.constants

import android.content.Context
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService

object Tokens {
    var accessToken: String? = null
    var CurrentAuthState: AuthState? = null
    var authService: AuthorizationService? = null
    var context: Context? = null
}

