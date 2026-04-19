package com.asue24.gitlab.domain.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.domain.utility.constants.AuthConfig
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import com.asue24.gitlab.domain.utility.constants.GitlabRefreshToken
import com.asue24.gitlab.domain.utility.constants.Tokens
import com.asue24.gitlab.data.remote.ApolloService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest

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
)  {
     try {
         Log.d("stored refreshToken value is", refreshToken)
         var Projects: GetMyProjectsQuery.ProjectMemberships?
         if (authState == null) {
             throw NullPointerException("AuthState is null")
         }
         if (authState.authorizationServiceConfiguration == null) throw NullPointerException("authorizationServiceConfiguration is null")
         val request = authState.createTokenRefreshRequest()
         service.performTokenRequest(request) { tokenResponse, ex ->
             if (tokenResponse != null) {
                 Tokens.accessToken = tokenResponse.accessToken
                 val newRefreshToken = tokenResponse.refreshToken
                 val apolloClient = ApolloService.setUpApolloClient(accessToken = tokenResponse.accessToken!!)
                 val scope = CoroutineScope(Dispatchers.IO)
                 scope.launch {
                     withContext(Dispatchers.Main) {
                         AuthStorage.getInstance(context).updateData {
                             GitlabRefreshToken(newRefreshToken)
                         }
                         Projects=apolloClient.query(GetMyProjectsQuery()).execute().data?.currentUser?.projectMemberships
                         Log.e("projects","$Projects.")
                     }
                 }
             } else {
                 Log.e("OAUTH_REFRESH", "Refresh failed: ${ex?.errorDescription}")
                 throw Exception(ex)
             }
         }
     }
     catch (e:Exception){
         throw e
     }
     finally {
         Log.d("finally","finally bolcok")
     }

}
