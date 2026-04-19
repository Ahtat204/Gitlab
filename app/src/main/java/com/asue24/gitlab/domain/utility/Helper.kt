package com.asue24.gitlab.domain.utility

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService

fun buildResponse(
    intent: Intent, authRequest: AuthorizationRequest?, context: Context
): AuthorizationResponse? {
    var response: AuthorizationResponse? = null
    val uri = intent?.data
    if (uri != null) {
        response = AuthorizationResponse.Builder(authRequest!!).fromUri(uri).build()
        val ex = AuthorizationException.fromIntent(intent)
        if (ex != null) {
            Log.e(
                "OAUTH_ERROR", "Code: ${ex.code}, Type: ${ex.type}, Message: ${ex.errorDescription}"
            )

            Toast.makeText(context, "Error: ${ex.errorDescription}", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("error ", "OAUTH_ERROR")
        }

        return response
    }
    return null
}

fun refreshAccessToken(
    authState: AuthState?, service: AuthorizationService, refreshToken: String, context: Context
) {
    try {
        Log.d("stored refreshToken value is", refreshToken)
        if (authState == null) {
            return
        }
        if (authState.authorizationServiceConfiguration == null) return
        authState.performActionWithFreshTokens(service) { accessToken, idToken, ex ->
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch { AuthStorage.getAuthState(context).updateData { authState } }
        }
    } catch (e: Exception) {
        throw e
    } finally {
        Log.d("finally", "finally bolcok")
    }

}
