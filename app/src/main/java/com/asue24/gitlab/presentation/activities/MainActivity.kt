package com.asue24.gitlab.presentation.activities

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.presentation.components.BottomBar
import com.asue24.gitlab.presentation.navigation.BottomNavigationgraph
import com.asue24.gitlab.presentation.ui.theme.GitlabTheme
import java.io.File



class MainActivity : ComponentActivity() {
    public val counter:Int=0
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, true)
       // enableEdgeToEdge(statusBarStyle =SystemBarStyle.dark(Color.BLACK),SystemBarStyle.auto(Color.BLACK, Color.BLACK))
        Tokens.context = application
        val mFolder = File(Environment. getExternalStorageDirectory().path+"gitlab/httpCache")
        if (!mFolder.exists()) {
            mFolder.mkdir()
        }
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(),bottomBar = {
                    BottomBar(navController)
                }, floatingActionButtonPosition = FabPosition.End) { x ->
                    BottomNavigationgraph(navController,x)
                }
            }
        }
    }


}