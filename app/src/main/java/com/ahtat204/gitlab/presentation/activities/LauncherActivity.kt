package com.ahtat204.gitlab.presentation.activities

import android.R.anim
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ahtat204.gitlab.domain.usecase.authentication.AuthStorage
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
import com.ahtat204.gitlab.domain.usecase.logging.logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService

/**
 * LauncherActivity is the entry point of the application.
 *
 * ## Responsibilities
 * - Displays the splash screen while authentication state is being checked.
 * - Initializes [net.openid.appauth.AuthorizationService] and sets up token context in [com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens].
 * - Ensures cache directory (`gitlab/httpCache`) exists for Apollo/HTTP caching.
 * - Determines whether to navigate to [MainActivity] (authenticated) or
 *   [AuthenticationActivity] (login required).
 *
 * ## Lifecycle
 * - **onCreate**:
 *   - Installs splash screen.
 *   - Initializes authentication service and token context.
 *   - Launches coroutine to check stored authentication state.
 *   - If a valid refresh token exists, attempts to refresh access token.
 *   - Navigates to the appropriate activity based on authentication outcome.
 * - **onDestroy**:
 *   - Disposes of [net.openid.appauth.AuthorizationService] to release resources.
 *
 * ## Error Handling
 * - If token refresh fails, navigates to [AuthenticationActivity].
 * - Exceptions during token refresh are rethrown after navigation.
 *
 * ## Navigation
 * - [navigateTo]: Helper method to start a new activity with transition animation
 *   and finish the launcher.
 *
 * ## Usage
 * This activity is automatically launched at app startup. It should not be
 * started manually from other parts of the app.
 * @author Lahcen AHTAT
 */
@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {
    private lateinit var authenticationService: AuthorizationService

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        authenticationService = AuthorizationService(this)
        var isReady = false
        splashScreen.setKeepOnScreenCondition { isReady }
        CoroutineScope(Dispatchers.IO).launch {
            val storedState = AuthStorage.getAuthState(this@LauncherActivity).data.first()
            if (storedState.isAuthorized) {
                storedState.performActionWithFreshTokens(authenticationService) { token, _, ex ->
                    if (token != null && ex == null) {
                        Tokens.accessToken = token
                        Tokens.CurrentAuthState = storedState
                        lifecycleScope.launch {
                            AuthStorage.getAuthState(this@LauncherActivity)
                                .updateData { storedState }
                            isReady = true
                            navigateTo(MainActivity::class.java)
                        }
                    }
                    if (ex != null) {
                        logger(ex.message)
                        navigateTo(AuthenticationActivity::class.java)
                        throw ex
                    }
                }
            } else {
                navigateTo(AuthenticationActivity::class.java)

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun navigateTo(destination: Class<*>) {
        startActivity(Intent(this, destination))
        overrideActivityTransition(
            OVERRIDE_TRANSITION_OPEN, anim.fade_in, anim.fade_out
        )
        finish()
    }

    override fun onDestroy() {
        authenticationService.dispose()
        super.onDestroy()
    }
}