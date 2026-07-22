package com.ahtat204.gitlab.presentation.components

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context

object CoilCache {
    lateinit var loader: ImageLoader
        private set
    fun init(coilLoader: ImageLoader) {
        loader = coilLoader
    }
}

