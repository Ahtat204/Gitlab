package com.asue24.gitlab.domain.utility.constants

import net.openid.appauth.ResponseTypeValues

/**
 * Configuration data for the auth flow
 */
object AuthConfig {
    const val AUTH_URI = "https://gitlab.com/oauth/authorize"
    const val TOKEN_URI = "https://gitlab.com/oauth/token"
    const val END_SESSION_URI = "https://gitlab.com/logout"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "read_api read_user read_repository"
    val CLIENT_ID = ""
    const val CLIENT_SECRET = ""
    const val CALLBACK_URL = "com.asue24.gitlab://oauth2redirect"
    const val LOGOUT_CALLBACK_URL = ""
}