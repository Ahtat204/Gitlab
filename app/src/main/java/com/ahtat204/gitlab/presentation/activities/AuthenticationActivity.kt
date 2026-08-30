package com.ahtat204.gitlab.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.domain.usecase.authentication.authStateStore
import com.ahtat204.gitlab.domain.usecase.authentication.constants.AuthConfig
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

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
 * @author Lahcen AHTAT
 */
class AuthenticationActivity : ComponentActivity() {

    private var serviceConfig: AuthorizationServiceConfiguration? =
        AuthorizationServiceConfiguration(
            AuthConfig.AUTH_URI.toUri(), AuthConfig.TOKEN_URI.toUri()
        )

    private var response: AuthorizationResponse? = null
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
                Modifier
                    .offset(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "logo",
                    Modifier
                        .size(150.dp)
                        .padding(0.dp), tint = Orange
                )
                Spacer(modifier = Modifier.height(120.dp))
                if (response == null) Button(onClick = {
                    val authIntent = getService().getAuthorizationRequestIntent(authRequest!!)
                        ?: throw NullPointerException("Intent is null")
                    launcher.launch(authIntent)
                    authState = AuthState(serviceConfig!!)
                }) {
                    Text(text = "Login With Gitlab", fontSize = 30.sp)
                }
                else CircularProgressIndicator()
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
        response = buildResponse(intent, authRequest, this)
        if (response == null) {
            logger("AuthenticationActivity", "OAUTH_ERROR")
            return
        }
        response?.let { runBlocking { exchangeCodeForToken(getService(), it, authState!!) } }
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
                Tokens.CurrentAuthState = authState
            }
        }
    }

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
     * @author Lahcen AHTAT
     */
    private fun buildResponse(
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
                    "OAUTH_ERROR",
                    "Code: ${ex.code}, Type: ${ex.type}, Message: ${ex.errorDescription}"
                )
                Toast.makeText(context, "Error: ${ex.errorDescription}", Toast.LENGTH_SHORT).show()
            } else {
                logger("error ", "OAUTH_ERROR")
            }
            return response
        }
        return null
    }
}
