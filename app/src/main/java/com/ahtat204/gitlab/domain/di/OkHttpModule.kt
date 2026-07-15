package com.ahtat204.gitlab.domain.di

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ahtat204.gitlab.BuildConfig
import com.ahtat204.gitlab.data.security.AuthenticationInterceptor
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
 * @author Lahcen AHTAT
 */
@InstallIn(SingletonComponent::class)
@Module
object OkHttpModule {
    @Singleton
    @Provides
    fun provieOkHttp(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).cache(
            cache = Cache(
                context.cacheDir, 10L * 1024 * 1024
            )
        ).readTimeout(15, TimeUnit.SECONDS).addInterceptor(AuthenticationInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).build()
    }

    /**
     *
     */
    @Singleton
    @Provides
    fun provideImageLoader(okHttpClient: OkHttpClient): ImageLoader {
        return ImageLoader.Builder(context).crossfade(true).dispatcher(Dispatchers.IO)
            .respectCacheHeaders(false).okHttpClient(okHttpClient).memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25) // Use 25% of app's memory for images
                    .build()
            }.diskCache {
                DiskCache.Builder().directory(context.cacheDir?.resolve("image_cache")!!)
                    .maxSizeBytes(50L * 1024 * 1024).build()
            }.build()
    }
}
