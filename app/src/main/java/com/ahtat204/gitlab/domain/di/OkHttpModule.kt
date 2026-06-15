package com.ahtat204.gitlab.domain.di

import android.util.Log
import com.ahtat204.gitlab.data.security.AuthenticationInterceptor
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.accessToken
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
/**
 * Dagger Hilt module that provides a preconfigured singleton instance of [OkHttpClient].
 *
 * ## Purpose
 * This module centralizes the configuration of the HTTP client used across the app
 * for both **Apollo GraphQL** and **Retrofit REST** requests. By sharing a single
 * [OkHttpClient], the app avoids creating multiple socket connections, reducing
 * battery drain and ensuring efficient reuse of underlying TCP connections.
 *
 * ## Lifecycle
 * - Scoped to [SingletonComponent], ensuring a single instance throughout the app.
 *
 * ## Configuration
 * - **Timeouts**: Sets connection and read timeouts to 15 seconds.
 * - **Authentication**: Adds [AuthenticationInterceptor] to handle GitLab token rotation.
 * - **Logging**: Adds [HttpLoggingInterceptor] configured to log HTTP headers for debugging.
 *
 * ## Usage
 * The provided [OkHttpClient] can be injected wherever needed:
 *
 * ### GraphQL (Apollo)
 * ```kotlin
 * ApolloClient.Builder()
 *     .okHttpClient(okHttpClient)
 *     .build()
 * ```
 *
 * ### REST (Retrofit)
 * ```kotlin
 * Retrofit.Builder()
 *     .client(okHttpClient)
 *     .build()
 * ```
 *
 * ## Notes
 * - This module does not configure caching (e.g., [MemoryCacheFactory]) directly,
 *   but the provided client can be extended with caching if required.
 * - Interceptors are applied in the order they are added: logging first, then authentication.
 */
@InstallIn(SingletonComponent::class)
@Module
object OkHttpModule {
    @Singleton
    @Provides
    fun provieOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS).cache(cache = Cache(
            Tokens.context.cacheDir,
            10L * 1024 * 1024))
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor() .apply {

                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor(AuthenticationInterceptor()).build()
    }
}
