package com.ahtat204.gitlab.domain.usecase.authentication.security

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.ahtat204.gitlab.domain.usecase.authentication.AuthStateSerializer
import net.openid.appauth.AuthState

/**
 * Provides a secure [DataStore] instance for persisting and retrieving
 * [AuthState] objects using the [com.ahtat204.gitlab.domain.usecase.authentication.AuthStateSerializer].
 *
 * This setup ensures that OAuth authentication state (tokens, refresh state,
 * etc.) is stored in an encrypted format and can be accessed consistently
 * throughout the application.
 *
 * ## Components
 * - `authStateStore`: An extension property on [Context] that creates a
 *   DataStore backed by the file `auth_state.txt` and uses [com.ahtat204.gitlab.domain.usecase.authentication.AuthStateSerializer]
 *   for secure serialization.
 * - `AuthStorage`: A singleton-style object that manages a global reference
 *   to the [DataStore]. It ensures that only one instance of the DataStore
 *   is created and reused across the app.
 *
 * ## Usage
 * - Call `AuthStorage.getAuthState(context)` to obtain the [DataStore] instance.
 * - Use the returned DataStore to read or update the current [AuthState].
 *
 * Example:
 * ```kotlin
 * val authStateStore = AuthStorage.getAuthState(context)
 * authStateStore.updateData { currentState ->
 *     currentState.apply { /* update tokens */ }
 * }
 * ```
 * @author Lahcen AHTAT
 */
val Context.authStateStore: DataStore<AuthState> by dataStore(
    fileName = "auth_state.txt", serializer = AuthStateSerializer
)

/**
 * Singleton object that provides access to the global [AuthState] DataStore.
 *
 * Ensures that the DataStore instance is initialized only once and reused
 * throughout the application lifecycle.
 */
object AuthStorage {
    private var GlobalAuthState: DataStore<AuthState>? = null

    /**
     * Returns the global [DataStore] instance for [AuthState].
     *
     * If the DataStore has not yet been initialized, it will be created
     * using the provided [context].
     *
     * @param context The application context used to initialize the DataStore.
     * @return A [DataStore] instance for persisting [AuthState].
     */
    fun getAuthState(context: Context): DataStore<AuthState> {
        if (GlobalAuthState == null) {
            GlobalAuthState = context.authStateStore
        }
        return GlobalAuthState!!
    }
}


