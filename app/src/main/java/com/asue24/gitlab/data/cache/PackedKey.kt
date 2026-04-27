package com.asue24.gitlab.data.cache

import java.util.concurrent.ConcurrentHashMap

@JvmInline

value class PackedKey(val data: Long) {
    /**
     * this will be used to prevent the cache from exploding
     */
    val order: Int get() = (data and 0xFFFFFFFFL).toInt()

    /**
     * this the actual Key in the Cache
     */
    val idHash: Int get() = (data shr 32).toInt()

}
