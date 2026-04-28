package com.asue24.gitlab.presentation.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.presentation.components.BottomBar
import com.asue24.gitlab.presentation.components.ProjectDetailScreen
import com.asue24.gitlab.presentation.navigation.BottomNavigationgraph
import com.asue24.gitlab.presentation.ui.theme.GitlabTheme
import com.asue24.gitlab.presentation.viewmodels.ProjectViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var Projects: GetRepoTreeQuery.Project? = null
    private val projectViewModel: ProjectViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isReady = false
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            Log.d("AccessToken", Tokens.accessToken.toString())
            val authState = AuthStorage.getAuthState(this@MainActivity).data.first()
            isReady = true
            Projects = ApolloService.setUpApolloClient()
                .query(GetRepoTreeQuery("Ahtat204/e-store-orderservice", ""))
                .execute().data?.project
            Log.d("OrderService", Projects.toString())
        }
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !isReady }
        installSplashScreen()
        Tokens.context = application
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomBar(navController)
                }, floatingActionButtonPosition = FabPosition.End) { x ->
                    BottomNavigationgraph(navController)
                    ProjectDetailScreen(
                        "Ahtat204/e-store-orderservice", ""
                    )
                }
            }
        }
    }

}/*

@Composable
fun ApiScreen() {
    var Projects by remember { mutableStateOf< GetMyProjectsQuery. ContributedProjects?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // LaunchedEffect runs once when key is Unit (first composition)
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            val result = withContext(Dispatchers.IO) {
                Log.d("AccessToken", Tokens.accessToken.toString())
                val apolloClient = ApolloService.setUpApolloClient()
             return@withContext    apolloClient.query(GetMyProjectsQuery())
                    .execute().data?.currentUser?.contributedProjects
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
                Text(Projects?.nodes?.get(1)?.project?.description!!, color = Color.White)
            }
        }
    }
}
*/