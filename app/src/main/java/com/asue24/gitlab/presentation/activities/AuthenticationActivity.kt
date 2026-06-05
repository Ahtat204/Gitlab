package com.asue24.gitlab.presentation.activities;

import android.content.Intent
import android.net.Uri
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.GitlabApp
import com.asue24.gitlab.data.repositories.AuthenticationRepository
import com.asue24.gitlab.domain.utility.buildResponse
import com.asue24.gitlab.domain.utility.constants.AuthConfig
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import com.asue24.gitlab.domain.utility.constants.Tokens
import com.asue24.gitlab.domain.utility.constants.authStateStore
import com.asue24.gitlab.domain.utility.refreshAccessToken
import com.asue24.gitlab.presentation.viewmodels.AuthenticationViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class AuthenticationActivity : ComponentActivity() {
    var serviceConfig: AuthorizationServiceConfiguration? = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI), Uri.parse(AuthConfig.TOKEN_URI)
    )
    var authRequest: AuthorizationRequest? = AuthorizationRequest.Builder(
        serviceConfig!!,
        AuthConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(AuthConfig.CALLBACK_URL)
    ).setScope(AuthConfig.SCOPE).build()
    private var authenticationService: AuthorizationService? = null
    val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private val AuthRepository: AuthenticationRepository by lazy { (application as GitlabApp).authRepo }
    var authState: AuthState? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = lifecycleScope
scope.launch {
    // 1. Get the ACTUAL state from the Flow (not the .toString() of the Flow)
    val authState = AuthStorage.getAuthState(this@AuthenticationActivity).data.first()

    // 2. Check if we actually have a valid session
    if (authState.isAuthorized) {

        // 3. If the token is missing/expired, WAIT for the refresh
        if (Tokens.accessToken == null) {
            try {
                refreshAccessToken(authState,getService(),authState.refreshToken!!,this@AuthenticationActivity)
            } catch (e: Exception) {
                return@launch
            }
        }

        // 4. ONLY NOW, after the refresh is DONE, move to MainActivity
        startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
        finish()
    } else {
        // No session found, stay here or show login
    }
}
        enableEdgeToEdge()
        installSplashScreen()




        authenticationViewModel = AuthenticationViewModel(AuthRepository)
        setContent {
            val navController = rememberNavController()
            Column(
                Modifier
                    .offset(100.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(modifier = Modifier, onClick = {
                    val authIntent = getService().getAuthorizationRequestIntent(authRequest!!)
                        ?: throw NullPointerException("Intent is null")
                    launcher.launch(authIntent)
                    authState = AuthState(serviceConfig!!)
                }) {
                    Text(text = "Login", fontSize = 30.sp, modifier = Modifier)
                }
            }
        }

    }

    fun getService(): AuthorizationService {
        if (authenticationService == null) {
            authenticationService = AuthorizationService(this)
        }
        return authenticationService!!
    }

    override fun onDestroy() {
        authenticationService?.dispose()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val response = buildResponse(
            intent, authRequest, this
        )
        if (response == null) {
            Log.e("error ", "OAUTH_ERROR")
            return
        }
        exchangeCodeForToken(getService(), response, authState!!)

    }

   fun exchangeCodeForToken(
        service: AuthorizationService, response: AuthorizationResponse, authState: AuthState
    ) {
        authState.update(response, null)
        val tokenRequest = response.createTokenExchangeRequest()
        service.performTokenRequest(tokenRequest) { tokenResponse, ex ->
            if (tokenResponse != null) {
                authState.update(tokenResponse, ex)
                lifecycleScope.launch { authStateStore.updateData { authState } }
                val accessToken = tokenResponse.accessToken
                Tokens.accessToken = accessToken
                val refreshToken = tokenResponse.refreshToken

                Log.d(
                    "result",
                    "AuthActivity :Refresh Token: $refreshToken \t Access Token: $accessToken"
                )
                lifecycleScope.launch {
                    val AuState=AuthStorage.getAuthState(this@AuthenticationActivity).data.first()
                    delay(20000)
                    refreshAccessToken(AuState,getService(),AuState.refreshToken.toString(),this@AuthenticationActivity)


                }
            } else {
                Log.d(
                    "Error is Null",
                    "${ex?.errorDescription} and reason is ${ex?.error} and message is ${ex?.code}"
                )
            }
        }
    }
}

