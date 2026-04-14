package com.asue24.gitlab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.lifecycle.lifecycleScope
import com.asue24.gitlab.constants.AuthConfig
import com.asue24.gitlab.constants.GitlabRefreshToken
import com.asue24.gitlab.constants.SafeStore.datastore
import com.asue24.gitlab.constants.Tokens
import kotlinx.coroutines.launch
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
    private var serviceConfig: AuthorizationServiceConfiguration?=null
    private var authenticationService: AuthorizationService? = null
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            Button({
                clickHandler()
            } ,modifier = Modifier.size(200.dp)) { Text(text = "Login") }
        }
    }

    private  fun exchangeCodeForToken(

        service: AuthorizationService,
        response: AuthorizationResponse,
        authState: AuthState
    ) {
        authState.update(response,null)
        val tokenRequest = response.createTokenExchangeRequest()
        service.performTokenRequest(tokenRequest) { tokenResponse, ex ->
            if (tokenResponse != null) {
                authState.update(tokenResponse, ex)
                val accessToken = tokenResponse.accessToken
                Tokens.accessToken=accessToken
                val expiresAt = tokenResponse.accessTokenExpirationTime
                val refreshToken = tokenResponse.refreshToken

                Log.d("result", "AuthActivity :Refresh Token: $refreshToken \t Access Token: $accessToken")
              lifecycleScope.launch {
                  datastore.updateData {
                      GitlabRefreshToken(refreshToken)
                  }
              }
                val intent = Intent(this, MainActivity::class.java)
                // Clear the back stack so the user can't "back" into the login screen
                 intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 startActivity(intent)
                onDestroy()
            }
            else{
                Log.d("Error is Null","${ex?.errorDescription} and reason is ${ex?.error} and message is ${ex?.code}")
            }
        }
    }

    private fun clickHandler() {
             serviceConfig = AuthorizationServiceConfiguration(
                Uri.parse(AuthConfig.AUTH_URI),
                Uri.parse(AuthConfig.TOKEN_URI)
            )
            authRequest = AuthorizationRequest.Builder(
                serviceConfig!!,
                AuthConfig.CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(AuthConfig.CALLBACK_URL)
            ).setScope(AuthConfig.SCOPE).build()

            val authIntent = getService().getAuthorizationRequestIntent(authRequest!!) ?: throw  NullPointerException("Intent is null")
        launcher.launch(authIntent)
        authState=AuthState(serviceConfig!!)
    }
    override fun onDestroy() {
        authenticationService?.dispose()
        super.onDestroy()
    }
    override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
        val uri=intent?.data
        if(uri!=null){
            val response = AuthorizationResponse.Builder(authRequest!!).fromUri(uri).build()
               val ex = AuthorizationException.fromIntent(intent)
        if (ex != null) {
            Log.e("OAUTH_ERROR", "Code: ${ex.code}, Type: ${ex.type}, Message: ${ex.errorDescription}")
            Toast.makeText(this, "Error: ${ex.errorDescription}", Toast.LENGTH_SHORT).show()
        }

        exchangeCodeForToken(getService(), response,authState!!)
    }
}
    private fun getService(): AuthorizationService {
        if (authenticationService == null) {
            authenticationService  = AuthorizationService(this)
        }
        return authenticationService!!
    }

}

