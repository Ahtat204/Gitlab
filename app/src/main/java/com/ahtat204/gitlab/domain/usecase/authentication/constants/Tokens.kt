package com.ahtat204.gitlab.domain.usecase.authentication.constants

import androidx.datastore.core.DataStore
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import net.openid.appauth.AuthState
/**
 * Singleton object that manages authentication tokens and application context.
 *
 * ## Purpose
 * - Provides a centralized place to store and access the current `accessToken`
 *   and `AuthState` for GitLab authentication.
 * - Ensures that the application context is initialized safely to avoid memory leaks.
 *
 * ## Properties
 * - [accessToken]: The current access token used for authenticated requests.
 * - [CurrentAuthState]: The current authentication state, if available.
 * - [context]: Lazily provides the application context. Throws [IllegalStateException]
 *   if accessed before initialization.
 *
 * ## Initialization
 * Call [initialize] once at application startup with the `Application` context:
 * ```kotlin
 * Tokens.initialize(applicationContext)
 * ```
 * This strips away any `Activity` wrappers, ensuring zero memory leaks.
 *
 * ## Usage
 * - Access `Tokens.accessToken` when making authenticated API calls.
 * - Use `Tokens.CurrentAuthState` to check or update the current authentication state.
 * - Access `Tokens.context` for operations requiring a safe application context.
 *
 * ## Notes
 * - Attempting to access [context] before calling [initialize] will throw an exception.
 * - This object is designed to be thread-safe and used across the entire app lifecycle.
 * @author Lahcen AHTAT
 */
object Tokens {
    fun  isConnected(): Boolean
        {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

    /**
     * Thread-safe cached access token updated on app launch (not every app start only if app start and access token is expired)
     * it's [Volatile] to eliminate any race condition,cached to avoid reading it from disk ([DataStore] and [AuthState]) everytime
     */
    @Volatile
    var accessToken: String? = null
    /**
     * an in-memory cached thread-safe(theoretically) Global instance of the OpenID [AuthState] to be used for getting new access tokens without hitting the disk ([DataStore])
     */
    @Volatile
    var CurrentAuthState: AuthState? = null
    private var appContext: Context? = null
    val context: Context
        get() = appContext ?: throw IllegalStateException("Tokens object must be initialized with Application Context first.")
    fun initialize(context: Context) {
        if (appContext == null) {
            // .applicationContext strips away any Activity wrappers, ensuring zero memory leaks
            appContext = context.applicationContext
        }
    }
}

