package com.asue24.gitlab.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.presentation.ui.theme.GitlabTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            GitlabTheme(darkTheme = true) {
                Text(text="MainAcitivity", fontSize = 80.sp)



            }
        }
        val scope= CoroutineScope(Dispatchers.IO)
        scope.launch {
            Log.d("AccessToken", Tokens.accessToken.toString())
            delay(50000)
           val authState= AuthStorage.getAuthState(this@MainActivity).data.first()
            Log.d("accessToken from AuthAct", Tokens.accessToken.toString()+"refresh token is"+Tokens.accessToken.toString()+"from AuthState"+authState.accessToken)
        }



    }
}
