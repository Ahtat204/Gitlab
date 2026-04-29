package com.asue24.gitlab.data.remote

import android.util.Log
import com.apollographql.apollo.cache.normalized.api.CacheKey
import com.apollographql.apollo.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo.cache.normalized.api.CacheKeyGeneratorContext

/**
 * this object is used to allow GraphQL Normalized Cache to store project objects with the key being the Project ID , without this , it will either Throw a CacheMissException or return null
 */
val cacheKeyGenerator = object : CacheKeyGenerator {
    override fun cacheKeyForObject(
        obj: Map<String, Any?>,
        context: CacheKeyGeneratorContext
    ): CacheKey?  {
        val typeName = obj["__typename"] as? String
        val id = obj["id"] as? String

        return if (typeName != null && id != null) {
            CacheKey("$typeName:$id")
        } else if (typeName == "QueryRoot") {
            // Root queries need a key too!
            val fieldName = context.field.name  // "project"
            val variables = context.variables
            CacheKey("QueryRoot:$fieldName:$variables")
        } else {
            null
        }
    }
}

