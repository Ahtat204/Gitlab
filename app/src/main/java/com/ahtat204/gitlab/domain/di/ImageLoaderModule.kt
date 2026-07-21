package com.ahtat204.gitlab.domain.di

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Dagger/Hilt module provider for the global [ImageLoader] instance.
 *
 * ## Overview
 * - Configures **Coil** as the image loading library across the application.
 * - Annotated with [@Singleton] to ensure a single, shared instance manages memory and disk caches app-wide,
 *   preventing redundant resource allocations and cache fragmentation.
 *
 * ## Configuration Highlights
 * - **Crossfade:** Enabled for smooth UI transitions when loading images.
 * - **Dispatcher:** Configured to [Dispatchers.IO] to offload decoding and loading from the main thread.
 * - **Memory Cache:** Allocates 25% of the app's available heap memory via [MemoryCache].
 * - **Disk Cache:** Allocates a 50 MB bounded cache directory (`image_cache`) via [DiskCache].
 * - **Network:** Shares the unified [okhttp3.OkHttpClient] instance for connection pooling and authentication interceptors.
 *
 * @author Lahcen AHTAT
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
    /**
     * Provides a singleton instance of [ImageLoader] configured with custom memory, disk, and network policies.
     *
     * @param context The application context, injected via [@ApplicationContext].
     * @param okHttpClient The shared network client used for fetching remote images.
     * @return A fully configured, thread-safe [ImageLoader] instance.
     */
    @Provides
    @Singleton
    fun provideImageLoader(
        okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(context).crossfade(true).dispatcher(Dispatchers.IO)
            .respectCacheHeaders(false).okHttpClient(okHttpClient).memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25) // Use 25% of app's memory for images
                    .build()
            }.diskCache {
                DiskCache.Builder().directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50 MB cap
                    .build()
            }.build()
    }
}