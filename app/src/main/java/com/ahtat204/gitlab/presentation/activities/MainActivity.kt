package com.ahtat204.gitlab.presentation.activities

import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ahtat204.gitlab.presentation.components.BottomBar
import com.ahtat204.gitlab.presentation.navigation.BottomNavigationGraph
import com.ahtat204.gitlab.presentation.ui.theme.GitlabTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val mFolder = File(Environment.getExternalStorageDirectory().path + "gitlab/httpCache")
        if (!mFolder.exists()) {
            mFolder.mkdir()
        }
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomBar(navController)
                }, floatingActionButtonPosition = FabPosition.End) { x ->
                    BottomNavigationGraph(navController, x)
                }
            }
        }
    }

}