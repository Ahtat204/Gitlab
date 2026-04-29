package com.asue24.gitlab.data.remote

import android.util.Log
import com.apollographql.apollo.api.CompiledField
import com.apollographql.apollo.api.Executable
import com.apollographql.apollo.cache.normalized.api.CacheKey
import com.apollographql.apollo.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo.cache.normalized.api.CacheKeyGeneratorContext
import com.apollographql.apollo.cache.normalized.api.CacheKeyResolver

/**
 * this object is used to allow GraphQL Normalized Cache to store project objects with the key being the Project ID , without this , it will either Throw a CacheMissException or return null
 */
val ProjectCacheKeyResolver = object : CacheKeyGenerator {
    override fun cacheKeyForObject(
        obj: Map<String, Any?>, context: CacheKeyGeneratorContext
    ): CacheKey {
        Log.d("CacheKeyResolver", obj.toString())
        val result = obj["id"]
        return CacheKey(obj["id"] as String)
    }
}
val projectCacheKeyResolver=object:CacheKeyResolver(){
    override fun cacheKeyForField(
        field: CompiledField, variables: Executable.Variables
    ): CacheKey? {
        TODO("Not yet implemented")
    }
}