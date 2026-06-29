package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.domain.usecase.authentication.constants.AuthConfig.GRAPHQL_URL
import com.apollographql.apollo.api.http.DefaultHttpRequestComposer
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.network.http.DefaultHttpEngine
import com.apollographql.apollo.network.http.HttpNetworkTransport
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Dagger Hilt module providing a configured [ApolloClient] instance.
 *
 * ## Overview
 * - Centralizes Apollo client configuration for GitLab GraphQL API.
 * - Ensures a singleton lifecycle via Hilt’s [SingletonComponent].
 * - Adds authentication and logging interceptors to the underlying OkHttp client.
 * - Configures normalized in‑memory caching for query results.
 *
 * ## Key Features
 * - **Authorization header**: Injects a bearer token from [Tokens.accessToken].
 * - **Logging**: Uses [HttpLoggingInterceptor] to log HTTP headers for debugging.
 * - **AuthenticationInterceptor**: Custom interceptor for handling GitLab auth.
 * - **Normalized cache**: Backed by [MemoryCacheFactory] with a 10 MB limit and
 *   60‑second expiration.
 *
 * ## Usage
 * The provided Apollo client can be injected into repositories or use‑cases:
 * ```kotlin
 * @Inject lateinit var apolloClient: ApolloClient
 *
 * val projects = apolloClient.query(GetMyProjectsQuery()).execute()
 * ```
 * @author Lahcen AHTAT
 */
@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {
    private val cacheFactory = MemoryCacheFactory(
        maxSizeBytes = 20 * 1024 * 1024, expireAfterMillis = 600000
    )

    /**
     * Provides a singleton [ApolloClient] configured for GitLab GraphQL API.
     *
     * @return A fully configured Apollo client with authentication, logging,
     *         and normalized caching enabled.
     */
    @Singleton
    @Provides
    fun getApolloService(okHttpClient: OkHttpClient): ApolloClient {
        val httpEngine = DefaultHttpEngine { okHttpClient }
        val requestComposer = DefaultHttpRequestComposer(GRAPHQL_URL)
        val networkTransport = HttpNetworkTransport.Builder().httpEngine(httpEngine)
            .httpRequestComposer(requestComposer).build()
        return ApolloClient.Builder().networkTransport(networkTransport).normalizedCache(
                cacheFactory, writeToCacheAsynchronously = false
            ).build()
    }
}
