package com.asue24.gitlab.services

import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest

data class AuthenticationRequestObject(val authState: AuthState?,val authRequest: AuthorizationRequest?)
