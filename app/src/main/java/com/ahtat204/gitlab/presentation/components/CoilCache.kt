package com.ahtat204.gitlab.presentation.components

import coil.ImageLoader

object CoilCache {
    lateinit var loader: ImageLoader
        private set

    fun init(coilLoader: ImageLoader) {
        loader = coilLoader
    }
}

