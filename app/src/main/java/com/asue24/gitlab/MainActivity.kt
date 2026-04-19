package com.asue24.gitlab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.domain.utility.constants.Tokens
import com.asue24.gitlab.presentation.ui.theme.GitlabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Text(text="MainAcitivity", fontSize = 80.sp)
                Log.d("AccessToken",Tokens.accessToken.toString())
            }
        }

    }
}
