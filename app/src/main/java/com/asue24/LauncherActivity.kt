package com.asue24

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.asue24.gitlab.domain.authentication.AuthenticationActivity
import com.asue24.gitlab.domain.authentication.constants.AuthConfig
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import com.asue24.gitlab.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
class LauncherActivity : ComponentActivity() {
private lateinit var authenticationService:AuthorizationService
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install Splash Screen FIRST
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var isReady = false

        // 2. This is the "Magic Gate". It keeps the logo visible
        // as long as isReady is false.
        splashScreen.setKeepOnScreenCondition { !isReady }
authenticationService=AuthorizationService(this@LauncherActivity)
        // 3. Start the non-blocking check
        CoroutineScope(Dispatchers.Main).launch {
            val storedState = AuthStorage.getAuthState(this@LauncherActivity).data.first()

            if (storedState.refreshToken != null) {
                // We have a token! Try to refresh it.
                storedState.performActionWithFreshTokens(authenticationService) { token, _, ex ->
                    if (token != null && ex == null) {
                        Tokens.accessToken = token
                        Log.d("token is ",Tokens.accessToken!!)
                        lifecycleScope.launch {
                            AuthStorage.getAuthState(this@LauncherActivity).updateData { storedState }
                            navigateTo(MainActivity::class.java)
                        }
                    }
                    if(ex!=null) {
                    navigateTo(AuthenticationActivity::class.java)
                    throw ex
                    }
                }
            } else {
                // No token at all
                navigateTo(AuthenticationActivity::class.java)
            }
        }
    }

    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        finish()
    }

    override fun onDestroy() {
        authenticationService?.dispose()
        super.onDestroy()
    }
}
