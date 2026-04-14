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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.constants.AuthConfig
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.constants.GitlabRefreshToken
import com.asue24.gitlab.constants.Tokens
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.navigation.AppNavGraph
import com.asue24.gitlab.navigation.BottomBarScreen
import com.asue24.gitlab.navigation.UiState
import com.asue24.gitlab.screens.Home
import com.asue24.gitlab.screens.LoginScreen
import com.asue24.gitlab.ui.theme.GitlabTheme
import com.asue24.gitlab.utility.buildResponse
import com.asue24.gitlab.utility.exchangeCodeForToken
import com.asue24.gitlab.utility.refreshAccessToken
import com.asue24.gitlab.viewmodels.AuthenticationViewModel
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class MainActivity : ComponentActivity() {

    var serviceConfig: AuthorizationServiceConfiguration?= AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI)
    )
    var authRequest: AuthorizationRequest?= AuthorizationRequest.Builder(
        serviceConfig!!,
        AuthConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(AuthConfig.CALLBACK_URL)
    ).setScope(AuthConfig.SCOPE).build()
    private var authenticationService: AuthorizationService? = null
    val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> }
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private val AuthRepository: AuthenticationRepository by lazy { (application as GitlabApp).authRepo }
    var authState: AuthState? = null
    //  private var keepSplashOnScreen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
    //    splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        authenticationViewModel = AuthenticationViewModel(AuthRepository)
        setContent {
            val navController = rememberNavController()

            /*Button({
            }
                ,modifier = Modifier.size(200.dp)) { Text(text = "Login") }*/
            GitlabTheme (darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    AppNavGraph(navController, authenticationViewModel,this)
                }, floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate(route = BottomBarScreen.CreateTask.route) }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }, floatingActionButtonPosition = FabPosition.End) { x ->
                }
            }

        }
      if(Tokens.accessToken!=null){
          val apolloClient= ApolloService.setUpApolloClient(Tokens.accessToken!!)
          var Projects:GetMyProjectsQuery.ProjectMemberships?=null
          lifecycleScope.launch { Projects=apolloClient.query(GetMyProjectsQuery()).execute().data?.currentUser?.projectMemberships
          }
      }

    }
    fun getService(): AuthorizationService {
        if (authenticationService == null) {
            authenticationService  = AuthorizationService(this)
        }
        return authenticationService!!
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
        exchangeCodeForToken(getService(), response,authState!!,this)

    }

}

