package com.asue24.gitlab.data.remote

import android.util.Log
import com.asue24.gitlab.domain.authentication.constants.AuthStorage
import com.asue24.gitlab.domain.authentication.constants.Tokens
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

object Locker {
    @OptIn(InternalCoroutinesApi::class)
    private val locker = Any()
}

class TokenInterceptor : Interceptor {
    val authenticationService = Tokens.authService

    @OptIn(InternalCoroutinesApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("TokenInterceptor", "first log")
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
                if (accessToken != null && accessToken.equals(token) && state != null && Tokens.authService != null && Tokens.context != null) {
                    val deferred = CompletableDeferred<String?>()
                    runBlocking {
                        state.performActionWithFreshTokens(Tokens.authService!!) { token, _, ex ->
                            if (token != null && ex == null) {
                                Tokens.accessToken = token
                                Tokens.CurrentAuthState = state
                                Tokens.authService = authenticationService
                                deferred.complete(token)
                                CoroutineScope(Dispatchers.IO).launch {
                                    AuthStorage.getAuthState(Tokens.context!!).updateData { state }
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
                        Log.e("RefreshError", "couldn't refresh")
                    }
                }
            }

        }
        Log.d("Last Log", response.toString())
        return response
    }
}