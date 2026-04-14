package com.asue24.gitlab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.constants.Tokens
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.ui.theme.GitlabTheme
import com.asue24.gitlab.utility.refreshAccessToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val AuthRepository :AuthenticationRepository by lazy{(application as GitlabApp).authRepo}
    private var keepSplashOnScreen = true
    private val apolloClient = Tokens.accessToken?.let { ApolloService.setUpApolloClient(it) }
    private var Projects: GetMyProjectsQuery.ProjectMemberships? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        enableEdgeToEdge()
        lifecycleScope.launch {
            if(Tokens.accessToken==null){
                refreshAccessToken(authState = AuthRepository.authState.value, service = AuthRepository.authService, refreshToken = AuthStorage.getInstance(this@MainActivity).data.toString(),context = this@MainActivity)
            }
            delay(2000) // Simulate loading or animation
            keepSplashOnScreen = false
            if(AuthStorage.getInstance(this@MainActivity).data.toString().isEmpty()){}
        }
        if(AuthStorage.getInstance(this@MainActivity).data.toString().isEmpty()){
              val intent = Intent(this, AuthenticationActivity::class.java)
                // Clear the back stack so the user can't "back" into the login screen
                 intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 startActivity(intent)
        }

        lifecycleScope.launch {
            Projects = apolloClient?.query(GetMyProjectsQuery())
                ?.execute()?.data?.currentUser?.projectMemberships
        }


        Log.e("", "$Projects.")
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController,this,AuthRepository)
            GitlabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                }
            }
        }
    }
}
