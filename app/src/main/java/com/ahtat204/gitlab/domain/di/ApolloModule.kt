package com.ahtat204.gitlab.domain.di
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.network.okHttpClient
import com.ahtat204.gitlab.data.remote.AuthenticationInterceptor
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens
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
 */
@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    // In‑memory cache: 10 MB, entries expire after 60 seconds
    private val cacheFactory = MemoryCacheFactory(
        maxSizeBytes = 10 * 1024 * 1024,
        expireAfterMillis = 60000
    )

    /**
     * Provides a singleton [ApolloClient] configured for GitLab GraphQL API.
     *
     * @return A fully configured Apollo client with authentication, logging,
     *         and normalized caching enabled.
     */
    @Singleton
    @Provides
    fun GetApolloService(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://gitlab.com/api/graphql")
            .addHttpHeader("Authorization", "Bearer ${Tokens.accessToken}")
            .okHttpClient(OkHttpClient())
            .normalizedCache(
                cacheFactory,
                writeToCacheAsynchronously = false
            )
            .build()
    }
}
