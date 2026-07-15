package com.ahtat204.gitlab.data.security

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.ui.unit.IntOffset
import com.ahtat204.gitlab.domain.usecase.authentication.AuthStorage
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.isConnected
import com.ahtat204.gitlab.domain.usecase.logging.logger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationService
import okhttp3.Interceptor
import okhttp3.Response
import net.openid.appauth.AuthState
import okio.IOException

/**
 * An OkHttp [okhttp3.Interceptor] that attaches and refreshes OAuth access tokens
 * for authenticated requests against the GitLab API.
 *
 * This interceptor ensures that every outgoing request includes a valid
 * `Authorization` header. If a request fails with HTTP 401 (Unauthorized),
 * it attempts to refresh the access token using the current [AuthState].
 *
 * ## Behavior
 * - Adds the `Authorization: Bearer <token>` header if a token is available.
 * - If the response is `401 Unauthorized`:
 *   - Synchronizes on a lock to prevent concurrent refresh attempts.
 *   - Uses [net.openid.appauth.AuthState.performActionWithFreshTokens] to refresh the token.
 *   - Updates [com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.accessToken] and persists the new state via [com.ahtat204.gitlab.domain.usecase.authentication.AuthStorage].
 *   - Retries the request with the refreshed token.
 *   - Logs an error if the retry still fails with `401`.
 *
 * ## Concurrency
 * - A `Locker` object is used to synchronize token refresh operations,
 *   preventing race conditions when multiple requests fail simultaneously.
 *
 * ## Persistence
 * - After a successful refresh, the updated [AuthState] is saved into
 *   [com.ahtat204.gitlab.domain.usecase.authentication.AuthStorage] using DataStore, ensuring the new token is available
 *   for future requests.
 *
 * ## Usage
 * Register this interceptor with OkHttp:
 * ```kotlin
 * val client = OkHttpClient.Builder()
 *     .addInterceptor(AuthenticationInterceptor())
 *     .build()
 * ```
 *
 * @constructor Creates an interceptor that manages authentication headers
 * and token refresh logic for GitLab API requests.
 * @author Lahcen AHTAT
 */
class AuthenticationInterceptor : Interceptor {
    private val Locker = Any()

    @OptIn(InternalCoroutinesApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        try {
            if (!isConnected()) {
                throw IOException("no internet connection")
            }
            var request = chain.request()
            val builder = request.newBuilder()
            val token = Tokens.accessToken
            if (token != null) {
                builder.header("Authorization", "Bearer $token")
            }
            request = builder.build()
            var response = chain.proceed(request)

            if (response.code == 401) {
                synchronized(Locker) {
                    val state = Tokens.CurrentAuthState
                    val accessToken = Tokens.accessToken
                    if (accessToken != null && accessToken == token && state != null) {
                        val deferred = CompletableDeferred<String?>()
                        runBlocking {
                            state.performActionWithFreshTokens(AuthorizationService(context)) { token, _, ex ->
                                if (token != null && ex == null) {
                                    Tokens.accessToken = token
                                    Tokens.CurrentAuthState = state
                                    deferred.complete(token)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        AuthStorage.getAuthState(Tokens.context)
                                            .updateData { state }
                                    }
                                }
                                if (ex != null) {
                                    deferred.completeExceptionally(ex)
                                }
                            }
                            deferred.await()
                            response.close()
                        }
                        builder.header("Authorization", "Bearer ${Tokens.accessToken}")
                        request = builder.build()
                        response = chain.proceed(request)
                        if (response.code == 401) {
                            logger("RefreshError", "couldn't refresh")
                        }
                    }
                }
            }
            return response
        } catch (e: Exception) {

            throw e

        }

    }
}