package com.asue24.gitlab.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.presentation.ui.theme.GitlabTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private var Projects: GetMyProjectsQuery.ProjectMemberships? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isReady = false
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            Log.d("AccessToken", Tokens.accessToken.toString())
            val authState = AuthStorage.getAuthState(this@MainActivity).data.first()
            isReady=true
        }
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isReady }
        installSplashScreen()

        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                ApiScreen()
            }
        }


    }

}

@Composable
fun ApiScreen() {
    var Projects by remember { mutableStateOf< GetMyProjectsQuery. ProjectMemberships?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // LaunchedEffect runs once when key is Unit (first composition)
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            val result = withContext(Dispatchers.IO) {
                Log.d("AccessToken", Tokens.accessToken.toString())
                val apolloClient = ApolloService.setUpApolloClient(Tokens.accessToken!!)
             return@withContext    apolloClient.query(GetMyProjectsQuery())
                    .execute().data?.currentUser?.projectMemberships
            }
            Projects = result
        } catch (e: Exception) {
            errorMessage = e.localizedMessage ?: "Unknown error"
        } finally {
            isLoading = false
        }
    }

    // UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text("Error: $errorMessage")

            Projects != null -> Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Title: ${Projects?.nodes?.get(1)?.project?.name}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(Projects?.nodes?.get(1)?.project?.description!!)
            }
        }
    }
}