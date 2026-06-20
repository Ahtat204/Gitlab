package com.ahtat204.gitlab.domain.usecase.authentication.utility

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.ahtat204.gitlab.domain.usecase.logging.logger
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse

/**
 * Builds an [AuthorizationResponse] from the given [Intent] after a completed
 * OAuth authorization flow using AppAuth.
 *
 * This utility function extracts the redirect URI from the intent, reconstructs
 * the [AuthorizationResponse] using the provided [AuthorizationRequest], and
 * checks for any [AuthorizationException] that may have occurred during the
 * flow.
 *
 * ## Behavior
 * - If the intent contains a valid redirect URI, an [AuthorizationResponse]
 *   is built and returned.
 * - If an [AuthorizationException] is present, it is logged and a toast
 *   message is shown to the user.
 * - If no URI is found in the intent, `null` is returned.
 *
 * ## Logging
 * - Errors are logged with the tag `"OAUTH_ERROR"`.
 * - Exception details include code, type, and description.
 *
 * ## UI Feedback
 * - Displays a toast message with the error description when an
 *   [AuthorizationException] occurs.
 *
 * @param intent The [Intent] received from the OAuth redirect.
 * @param authRequest The original [AuthorizationRequest] used to initiate
 * the authorization flow. Must not be null if a URI is present.
 * @param context The [Context] used to display error messages via Toast.
 *
 * @return A valid [AuthorizationResponse] if the URI is present and parsed
 * successfully, or `null` if no URI is found.
 */
fun buildResponse(
    intent: Intent,
    authRequest: AuthorizationRequest?,
    context: Context
): AuthorizationResponse? {
    var response: AuthorizationResponse? = null
    val uri = intent.data
    if (uri != null) {
        response = AuthorizationResponse.Builder(authRequest!!).fromUri(uri).build()
        val ex = AuthorizationException.fromIntent(intent)
        if (ex != null) {
           logger(
                 "Code: ${ex.code}, Type: ${ex.type}, Message: ${ex.errorDescription}"
            )
            Toast.makeText(context, "Error: ${ex.errorDescription}", Toast.LENGTH_SHORT).show()
        } else {
           logger( "OAUTH_ERROR")
        }
        return response
    }
    return null
}
