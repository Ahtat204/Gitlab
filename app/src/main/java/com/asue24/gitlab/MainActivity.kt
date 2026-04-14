package com.asue24.gitlab

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.constants.RefreshTokenSerializer
import com.asue24.gitlab.constants.SafeStore.datastore
import com.asue24.gitlab.constants.Tokens
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.ui.theme.GitlabTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {

    private var keepSplashOnScreen = true
    private val apolloClient = Tokens.accessToken?.let { ApolloService.setUpApolloClient(it) }
    private var Projects: GetMyProjectsQuery.ProjectMemberships? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        enableEdgeToEdge()
        lifecycleScope.launch {
            delay(2000) // Simulate loading or animation
            keepSplashOnScreen = false
        }


        lifecycleScope.launch {
            Projects = apolloClient?.query(GetMyProjectsQuery())
                ?.execute()?.data?.currentUser?.projectMemberships
        }


        Log.e("", "$Projects.")
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController)
            GitlabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                }
            }
        }
    }
}
