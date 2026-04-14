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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.constants.AuthConfig
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.constants.GitlabRefreshToken
import com.asue24.gitlab.constants.Tokens
import com.asue24.gitlab.navigation.AppNavGraph
import com.asue24.gitlab.utility.buildResponse
import com.asue24.viewmodels.AuthenticationViewModel
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class MainActivity : ComponentActivity() {
    private var authState: AuthState? = null
    private var serviceConfig: AuthorizationServiceConfiguration?= AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI)
    )
    private var authRequest: AuthorizationRequest?= AuthorizationRequest.Builder(
        serviceConfig!!,
        AuthConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(AuthConfig.CALLBACK_URL)
    ).setScope(AuthConfig.SCOPE).build()
    private var authenticationService: AuthorizationService? = null
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private val AuthRepository: AuthenticationRepository by lazy { (application as GitlabApp).authRepo }
    private var keepSplashOnScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        authenticationViewModel = AuthenticationViewModel(AuthRepository)
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController, authenticationViewModel,::LoginCLick)
            Button({
            }
                ,modifier = Modifier.size(200.dp)) { Text(text = "Login") }
        }
    }
    private fun getService(): AuthorizationService {
        if (authenticationService == null) {
            authenticationService  = AuthorizationService(this)
        }
        return authenticationService!!
    }

     fun exchangeCodeForToken(
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
                  AuthStorage.getInstance(this@MainActivity).updateData {
                      GitlabRefreshToken(refreshToken)
                  }
              }
            }
            else{
                Log.d("Error is Null","${ex?.errorDescription} and reason is ${ex?.error} and message is ${ex?.code}")
            }
        }
    }
    override fun onDestroy() {
        authenticationService?.dispose()
        super.onDestroy()
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val response= buildResponse(
            intent, authRequest,
            this
        )
        if (response == null){
            Log.e("error ","OAUTH_ERROR" )
            return }
        exchangeCodeForToken(getService(), response,authState!!)

    }


    private fun LoginCLick( ){
        val authIntent = getService().getAuthorizationRequestIntent(authRequest!!) ?: throw  NullPointerException("Intent is null")
        launcher.launch(authIntent)
        authState=AuthState(serviceConfig!!)
    }
}
