package com.asue24.gitlab.domain.authentication.utility

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
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
