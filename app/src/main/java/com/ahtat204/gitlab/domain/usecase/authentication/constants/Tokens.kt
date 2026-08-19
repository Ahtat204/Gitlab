package com.ahtat204.gitlab.domain.usecase.authentication.constants

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.CurrentAuthState
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.accessToken
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.initialize
import net.openid.appauth.AuthState

/**
 * Singleton object that manages authentication tokens, application context, and network connectivity state.
 *
 * ## Overview
 * This object serves as a centralized, thread-safe cache for sensitive authentication data
 * and providing a leak-safe [Context] for system services. By caching [accessToken] and
 * [CurrentAuthState] in memory, the app avoids expensive disk I/O operations (like reading from
 * DataStore) during frequent authenticated requests.
 *
 * ## Key Responsibilities
 * - **Auth Caching**: Stores [accessToken] and [AuthState] with [Volatile] visibility to prevent race conditions.
 * - **Context Management**: Safely holds the `ApplicationContext` to provide access to system services without leaking Activities.
 * - **Connectivity**: Provides a utility to check real-time internet availability.
 *
 * ## Lifecycle & Initialization
 * [initialize] **must** be called exactly once during application startup (typically in the `Application` class):
 * ```kotlin
 * class MyApp : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *         Tokens.initialize(this)
 *     }
 * }
 * ```
 *
 * @author Lahcen AHTAT
 */
object Tokens {
    /**
     * Checks if the device has an active internet connection with internet capability.
     *
     * @return `true` if a network is active and has [NetworkCapabilities.NET_CAPABILITY_INTERNET], `false` otherwise.
     * @throws IllegalStateException if accessed before [initialize] is called.
     */
    fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * A thread-safe, in-memory cache of the GitLab OAuth2 access token.
     *
     * Marked as [Volatile] to ensure visibility across threads. This token is typically
     * refreshed at app launch if expired and then cached here to avoid reading from disk
     * for every API call.
     */
    @Volatile
    var accessToken: String? = null

    /**
     * An in-memory, thread-safe global instance of the OpenID [AuthState].
     *
     * This holds the full authentication state (including refresh tokens and expiration times),
     * allowing the app to perform token refreshes without manual DataStore lookups.
     */
    @Volatile
    var CurrentAuthState: AuthState? = null
    private var appContext: Context? = null

    /**
     * Provides access to the application-level [Context].
     *
     * @throws IllegalStateException if accessed before [initialize] is called.
     */
    val context: Context
        get() = appContext
            ?: throw IllegalStateException("Tokens object must be initialized with Application Context first.")

    /**
     * Initializes the [Tokens] singleton with a safe [Context].
     *
     * It automatically extracts the [Context.getApplicationContext] to ensure that
     * no `Activity` or `Service` wrappers are held, preventing memory leaks.
     *
     * @param context The context used to extract the application context.
     */
    fun initialize(context: Context) {
        if (appContext == null) {
            appContext = context.applicationContext
        }
    }
}

