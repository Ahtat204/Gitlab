package com.ahtat204.gitlab.domain.usecase.authentication.constants

import com.ahtat204.gitlab.BuildConfig
import net.openid.appauth.ResponseTypeValues
/**
 * Centralized configuration constants for the GitLab OAuth authentication flow.
 *
 * This object defines the endpoints, client details, and parameters required
 * to initiate and complete the OAuth 2.0 authorization process using AppAuth.
 *
 * ## Components
 * - `AUTH_URI`: The GitLab authorization endpoint used to start the OAuth flow.
 * - `TOKEN_URI`: The GitLab token endpoint used to exchange the authorization
 *   code for access and refresh tokens.
 * - `END_SESSION_URI`: The GitLab logout endpoint used to terminate sessions.
 * - `RESPONSE_TYPE`: The OAuth response type. Set to [ResponseTypeValues.CODE]
 *   to use the Authorization Code flow.
 * - `SCOPE`: The set of permissions requested during authentication. Includes
 *   API access, user info, repository access, and OpenID Connect.
 * - `CLIENT_ID`: The client identifier registered in GitLab, injected from
 *   [BuildConfig].
 * - `CALLBACK_URL`: The redirect URI registered with GitLab, used to receive
 *   the authorization response.
 *
 * ## Usage
 * These constants are consumed by AppAuth components such as
 * [AuthorizationRequest] and [AuthorizationService] to configure the
 * authentication flow.
 *
 * Example:
 * ```kotlin
 * val serviceConfig = AuthorizationServiceConfiguration(
 *     Uri.parse(AuthConfig.AUTH_URI),
 *     Uri.parse(AuthConfig.TOKEN_URI)
 * )
 * val authRequest = AuthorizationRequest.Builder(
 *     serviceConfig,
 *     AuthConfig.CLIENT_ID,
 *     AuthConfig.RESPONSE_TYPE,
 *     Uri.parse(AuthConfig.CALLBACK_URL)
 * ).setScope(AuthConfig.SCOPE).build()
 * ```
 */
object AuthConfig {
    /** GitLab authorization endpoint for initiating OAuth requests. */
    const val AUTH_URI = "https://gitlab.com/oauth/authorize"

    /** GitLab token endpoint for exchanging authorization codes. */
    const val TOKEN_URI = "https://gitlab.com/oauth/token"

    /** GitLab logout endpoint for ending user sessions. */
    const val END_SESSION_URI = "https://gitlab.com/logout"

    /** OAuth response type: Authorization Code flow. */
    const val RESPONSE_TYPE = ResponseTypeValues.CODE

    /** Requested scopes: API, user info, repository access, and OpenID. */
    const val SCOPE = "read_api read_user read_repository openid api"

    /** Client ID registered in GitLab, injected from BuildConfig. */
    const val CLIENT_ID = BuildConfig.CLIENT_ID

    /** Redirect URI registered with GitLab for OAuth callbacks. */
    const val CALLBACK_URL = "com.asue24.gitlab://oauth2redirect"
}
