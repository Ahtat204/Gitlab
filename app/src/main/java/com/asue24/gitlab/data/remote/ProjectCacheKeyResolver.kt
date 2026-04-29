package com.asue24.gitlab.data.remote

import com.apollographql.apollo.cache.normalized.api.CacheKey
import com.apollographql.apollo.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo.cache.normalized.api.CacheKeyGeneratorContext

/**
 * this object is used to allow GraphQL Normalized Cache to store project objects with the key being the Project ID , without this , it will either Throw a CacheMissException or return null
 */
val cacheKeyGenerator = object : CacheKeyGenerator {
    override fun cacheKeyForObject(
        obj: Map<String, Any?>, context: CacheKeyGeneratorContext
    ): CacheKey {
        val typeName = obj["__typename"] as? String  // "Project", "User", "Namespace"
        val id = obj["id"] as? String                // "123", "456"

        return if (typeName != null && id != null) {
            CacheKey("$typeName:$id")  // "Project:123", "User:456"
        } else {
            CacheKey.rootKey()  // Don't cache objects without identifiers
        }
    }
}

