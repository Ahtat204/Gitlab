package com.asue24.gitlab.domain.usecase.services

import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest

data class AuthenticationRequestObject(val authState: AuthState?,val authRequest: AuthorizationRequest?)
