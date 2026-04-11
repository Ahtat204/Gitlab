package com.asue24.gitlab.data.authentication

import java.lang.System.*

internal object OAuthConfig {
    const val AUTH_ENDPOINT = "https://example.com/oauth/authorize"
    const val TOKEN_ENDPOINT = "https://gitlab.com/oauth/token"
    val CLIENT_ID = getenv("CLIENT_ID")
    const val REDIRECT_URI = "com.example.oauthdemo:/oauth2redirect"
}