package com.ahtat204.gitlab.presentation.components

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context

object CoilCache {

    val customImageLoader = ImageLoader.Builder(context!!).memoryCache {
            MemoryCache.Builder(context!!)
                .maxSizePercent(0.25) // Use 25% of app's memory for images
                .build()
        }.diskCache {
            DiskCache.Builder().directory(context?.cacheDir?.resolve("image_cache")!!)
                .maxSizeBytes(50L * 1024 * 1024) // 50 MB
                .build()
        }.build()
}

