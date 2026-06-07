package com.ahtat204.gitlab.presentation.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ahtat204.gitlab.presentation.navigation.BottomBar
import com.ahtat204.gitlab.presentation.navigation.BottomNavigationGraph
import com.ahtat204.gitlab.presentation.ui.theme.GitlabTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point of the GitLab Android application(only after Authentication)
 *
 * This activity sets up:
 * - A splash screen on launch.
 * - System window compatibility for edge-to-edge UI.
 * - A local cache directory for HTTP requests.
 * - The Compose UI hierarchy with a themed [Scaffold].
 *
 * The UI includes:
 * - A [BottomBar] for navigation between screens.
 * - A [BottomNavigationGraph] to handle navigation destinations.
 *
 * Dependency injection is enabled via [AndroidEntryPoint] for Hilt.
 *
 * @constructor Creates the main activity for the GitLab app.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting.
     *
     * - Installs the splash screen.
     * - Configures system window behavior.
     * - Ensures the HTTP cache folder exists in external storage.
     * - Sets up the Compose UI with navigation and theming.
     *
     * @param savedInstanceState If the activity is being re-initialized
     * after previously being shut down, this contains the data it most
     * recently supplied in [onSaveInstanceState].
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Scaffold(
                    modifier = Modifier.fillMaxSize().background(Color.Black),
                    bottomBar = { BottomBar(navController) },
                    floatingActionButtonPosition = FabPosition.End
                ) { paddingValues ->
                    BottomNavigationGraph(navController, paddingValues)
                }
            }
        }
    }
}
