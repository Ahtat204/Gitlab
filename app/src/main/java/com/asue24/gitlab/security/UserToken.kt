package com.asue24.gitlab.security

/**
 * we Only encrypt the refresh token ,the access token is stored in memory
 */
data class UserToken(val refreshToken: String) {}