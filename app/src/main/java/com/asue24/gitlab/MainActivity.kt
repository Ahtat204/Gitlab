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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.domain.utility.constants.AuthConfig
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import com.asue24.gitlab.domain.utility.constants.GitlabRefreshToken
import com.asue24.gitlab.domain.utility.constants.Tokens
import com.asue24.gitlab.domain.utility.constants.authStateStore
import com.asue24.gitlab.presentation.navigation.AppNavGraph
import com.asue24.gitlab.presentation.navigation.BottomBarScreen
import com.asue24.gitlab.data.repositories.AuthenticationRepository
import com.asue24.gitlab.presentation.ui.theme.GitlabTheme
import com.asue24.gitlab.domain.utility.buildResponse
import com.asue24.gitlab.presentation.viewmodels.AuthenticationViewModel
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class MainActivity : ComponentActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        authenticationViewModel = AuthenticationViewModel(AuthRepository)
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    AppNavGraph(navController, authenticationViewModel, this, AuthRepository)
                }, floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate(route = BottomBarScreen.CreateTask.route) }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }, floatingActionButtonPosition = FabPosition.End) { x ->
                }
            }
        }/*     if(Tokens.accessToken!=null){
          val apolloClient= ApolloService.setUpApolloClient(Tokens.accessToken!!)
          var Projects:GetMyProjectsQuery.ProjectMemberships?=null
          lifecycleScope.launch { Projects=apolloClient.query(GetMyProjectsQuery()).execute().data?.currentUser?.projectMemberships
          }
      }*/

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
                lifecycleScope.launch { authStateStore.updateData { authState }}
                val accessToken = tokenResponse.accessToken
                Tokens.accessToken = accessToken
                val refreshToken = tokenResponse.refreshToken

                Log.d(
                    "result",
                    "AuthActivity :Refresh Token: $refreshToken \t Access Token: $accessToken"
                )
                lifecycleScope.launch {
                    AuthStorage.getInstance(this@MainActivity).updateData {
                        GitlabRefreshToken(refreshToken)
                    }
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

