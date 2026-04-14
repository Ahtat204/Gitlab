package com.asue24.gitlab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.ui.theme.GitlabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val apolloClient = ApolloService.setUpApolloClient()
    private var Projects: GetMyProjectsQuery.ProjectMemberships? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            Projects = apolloClient.query(GetMyProjectsQuery())
                .execute().data?.currentUser?.projectMemberships
        }

        Log.e("", "$Projects.")
        setContent {
            GitlabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                }
            }
        }
    }
}
