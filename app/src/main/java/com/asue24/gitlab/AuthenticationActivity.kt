package com.asue24.gitlab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues


class AuthenticationActivity : ComponentActivity() {
    private var authState: AuthState? = null
    private var authRequest: AuthorizationRequest?=null
    private val authenticationService by lazy {
        AuthorizationService(this)
    }
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    val data = result.data ?: return@registerForActivityResult

}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            Button({
                clickHandler()
            } ,modifier = Modifier.size(200.dp)) { Text(text = "Login") }
        }
    }

    private fun exchangeCodeForToken(

        service: AuthorizationService,
        response: AuthorizationResponse,
        authState: AuthState
    ) {
        authState.update(response,null)
        val additionalParams = mapOf("client_secret" to "")
        val tokenRequest = response.createTokenExchangeRequest(additionalParams)
        service.performTokenRequest(tokenRequest) { tokenResponse, ex ->
            if (tokenResponse != null) {
                authState.update(tokenResponse, ex)
                val accessToken = tokenResponse.accessToken
                val refreshToken = tokenResponse.refreshToken
                Log.d("Access Token: $accessToken", "Refresh Token: $refreshToken")
            }
            else{
                Log.d("Error is Null","${ex?.errorDescription} and reason is ${ex?.error} and message is ${ex?.code}")
            }
        }
    }

    private fun clickHandler() {
             val serviceConfig = AuthorizationServiceConfiguration(
                Uri.parse("https://gitlab.com/oauth/authorize"),
                Uri.parse("https://gitlab.com/oauth/token")
            )
        Log.d("xhszbhs","first lineeing")
            authRequest = AuthorizationRequest.Builder(
                serviceConfig,
                "",
                ResponseTypeValues.CODE,
                Uri.parse("com.asue24.gitlab://oauth2redirect")
            )
                .setScope("read_api read_user read_repository")
                .build()
        Log.d("xhzzerfdszbhs","first lineeing")
            val authenticationService = AuthorizationService(this)
        Log.d("xhzzerfdszbhs","first lineeing")
            val authIntent = authenticationService.getAuthorizationRequestIntent(authRequest!!)
        Log.d("xhzzerfdszbhs","first lineeing")
        Log.d("xhzzerfdszbhs","first lineeing")
        launcher.launch(authIntent)
        authState=AuthState(serviceConfig)
    }
    override fun onDestroy() {
        super.onDestroy()
        authenticationService.dispose()

    }
    override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    Log.d("DEBUG_INTENT", "Received Intent: ${intent.data}")
        val uri=intent?.data
        if(uri!=null){
            val response = AuthorizationResponse.Builder(authRequest!!).fromUri(uri).build()
               val ex = AuthorizationException.fromIntent(intent)
                Log.d("xhzzerfdszbhs","first lineeing")
        if (ex != null) {
            Log.e("OAUTH_ERROR", "Code: ${ex.code}, Type: ${ex.type}, Message: ${ex.errorDescription}")
        }
        // SUCCESS: Exchange the code for a token
        exchangeCodeForToken(authenticationService, response,authState!!)
        Log.d("xhzzerfdszbhs","first lineeing $response")
    }



}
}

