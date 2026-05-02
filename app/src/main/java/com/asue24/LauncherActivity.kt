package com.asue24

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.presentation.activities.AuthenticationActivity
import com.asue24.gitlab.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import java.io.File

class LauncherActivity : ComponentActivity() {
private lateinit var  authenticationService:AuthorizationService
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install Splash Screen FIRST
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val existed=File("gitlab/httpCache")
        if(!existed.exists()){
            existed.mkdir()
        }
        authenticationService= AuthorizationService(this)
        Tokens.authService=authenticationService
        Tokens.context=application
        var isReady = false
        splashScreen.setKeepOnScreenCondition { isReady }
        CoroutineScope(Dispatchers.IO).launch {
            val storedState = AuthStorage.getAuthState(this@LauncherActivity).data.first()

            if (storedState.refreshToken != null) {
                storedState.performActionWithFreshTokens(authenticationService) { token, _, ex ->
                    if (token != null && ex == null) {
                        Tokens.accessToken = token
                        Tokens.CurrentAuthState=storedState
                        Tokens.authService=authenticationService
                        lifecycleScope.launch {
                            AuthStorage.getAuthState(this@LauncherActivity).updateData { storedState }
                            isReady = true
                            navigateTo(MainActivity::class.java)

                        }
                    }
                    if(ex!=null) {
                    navigateTo(AuthenticationActivity::class.java)
                    throw ex
                    }
                }
            } else {
                navigateTo(AuthenticationActivity::class.java)
            }
        }
    }

    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        finish()
    }

    override fun onDestroy() {
        authenticationService.dispose()
        super.onDestroy()
    }
}
