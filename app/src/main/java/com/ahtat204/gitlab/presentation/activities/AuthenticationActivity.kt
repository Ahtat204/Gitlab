package com.ahtat204.gitlab.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.ahtat204.gitlab.domain.usecase.authentication.constants.AuthConfig
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
import com.ahtat204.gitlab.domain.usecase.authentication.authStateStore
import com.ahtat204.gitlab.domain.usecase.authentication.utility.buildResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import androidx.core.net.toUri

/**
 * Activity responsible for handling OAuth2 authentication flow with GitLab.
 *
 * ## Overview
 * - Uses [AppAuth](https://github.com/openid/AppAuth-Android) to perform OAuth2 login.
 * - Displays a simple Compose UI with a "Login" button.
 * - Initiates authorization request and exchanges authorization code for tokens.
 * - Persists authentication state in [authStateStore].
 * - Navigates to [MainActivity] upon successful login.
 *
 * ## Key Components
 * - [AuthorizationServiceConfiguration]: Holds auth and token endpoint URIs.
 * - [AuthorizationRequest]: Defines client ID, response type, scope, and callback URL.
 * - [AuthorizationService]: Executes authorization and token requests.
 * - [AuthState]: Tracks current authentication state and tokens.
 *
 * ## Lifecycle
 * - **onCreate**:
 *   - Sets up Compose UI with a login button.
 *   - Initializes authorization request and service.
 *   - Launches authorization intent when login is clicked.
 * - **onNewIntent**:
 *   - Handles redirect from OAuth callback.
 *   - Builds [AuthorizationResponse] and exchanges code for tokens.
 * - **onDestroy**:
 *   - Disposes of [AuthorizationService] to free resources.
 *
 * ## Error Handling
 * - Logs errors if authorization response is null.
 * - Token exchange exceptions are captured in [AuthState].
 *
 * ## Usage
 * This activity is launched when authentication is required. It should not be
 * started directly unless the user is unauthenticated.
 */
class AuthenticationActivity : ComponentActivity() {

    private var serviceConfig: AuthorizationServiceConfiguration? =
        AuthorizationServiceConfiguration(
            AuthConfig.AUTH_URI.toUri(), AuthConfig.TOKEN_URI.toUri()
        )

    private var authRequest: AuthorizationRequest? = AuthorizationRequest.Builder(
        serviceConfig!!,
        AuthConfig.CLIENT_ID,
        ResponseTypeValues.CODE, AuthConfig.CALLBACK_URL.toUri()
    ).setScope(AuthConfig.SCOPE).build()

    private var authenticationService: AuthorizationService? = null
    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    private var authState: AuthState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Compose UI with login button
        setContent {
            Column(
                Modifier.offset(10.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    val authIntent = getService().getAuthorizationRequestIntent(authRequest!!)
                        ?: throw NullPointerException("Intent is null")
                    launcher.launch(authIntent)
                    authState = AuthState(serviceConfig!!)
                }) {
                    Text(text = "Login With Gitlab", fontSize = 30.sp)
                }
            }
        }
    }

    /** Lazily initializes [AuthorizationService]. */
    private fun getService(): AuthorizationService {
        if (authenticationService == null) {
            authenticationService = AuthorizationService(this)
        }
        return authenticationService!!
    }

    override fun onDestroy() {
        authenticationService?.dispose()
        super.onDestroy()
    }

    /**
     * Handles OAuth2 redirect intent after user login.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val response = buildResponse(intent, authRequest, this)
        if (response == null) {
            Log.e("AuthenticationActivity", "OAUTH_ERROR")
            return
        }
        runBlocking { exchangeCodeForToken(getService(), response, authState!!) }
    }

    /**
     * Exchanges authorization code for access/refresh tokens.
     *
     * @param service The [AuthorizationService] instance.
     * @param response The [AuthorizationResponse] containing the auth code.
     * @param authState The current [AuthState].
     */
    private fun exchangeCodeForToken(
        service: AuthorizationService,
        response: AuthorizationResponse,
        authState: AuthState
    ) {
        authState.update(response, null)
        val tokenRequest = response.createTokenExchangeRequest()
        service.performTokenRequest(tokenRequest) { tokenResponse, ex ->
            if (tokenResponse != null) {
                authState.update(tokenResponse, ex)
                lifecycleScope.launch {
                    authStateStore.updateData { authState }
                    startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                    finish()
                }
                Tokens.accessToken = authState.accessToken
                Tokens.authService = authenticationService
                Tokens.CurrentAuthState = authState
            }
        }
    }
}
