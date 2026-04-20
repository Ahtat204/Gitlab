package com.asue24.gitlab.domain.authentication;

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
import com.asue24.gitlab.domain.authentication.constants.AuthConfig
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.domain.authentication.constants.authStateStore
import com.asue24.gitlab.domain.authentication.utility.buildResponse
import com.asue24.gitlab.domain.authentication.utility.refreshAccessToken
import com.asue24.gitlab.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class AuthenticationActivity : ComponentActivity() {
    private var serviceConfig: AuthorizationServiceConfiguration? =
        AuthorizationServiceConfiguration(
            Uri.parse(AuthConfig.AUTH_URI), Uri.parse(AuthConfig.TOKEN_URI)
        )
    private var authRequest: AuthorizationRequest? = AuthorizationRequest.Builder(
        serviceConfig!!,
        AuthConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(AuthConfig.CALLBACK_URL)
    ).setScope(AuthConfig.SCOPE).build()
    private var authenticationService: AuthorizationService? = null
    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private val AuthRepository: AuthenticationRepository by lazy { (application as GitlabApp).authRepo }
    private var authState: AuthState? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val authState = AuthStorage.getAuthState(this@AuthenticationActivity).data.first()
            if (authState.isAuthorized) {
                if (Tokens.accessToken == null) {
                    try {
                        refreshAccessToken(
                            authState,
                            getService(),
                            authState.refreshToken!!,
                            this@AuthenticationActivity
                        )
                    } catch (e: Exception) {
                        return@launch
                    }
                }
                startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                finish()
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
        runBlocking { exchangeCodeForToken(getService(), response, authState!!) }

    }

    private fun exchangeCodeForToken(
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
                lifecycleScope.launch {
                    val AuState = AuthStorage.getAuthState(this@AuthenticationActivity).data.first()
                 /*   delay(20000)
                    refreshAccessToken(AuState, getService(), AuState.refreshToken.toString(), this@AuthenticationActivity
                    )*/
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

