package com.asue24.gitlab.data.remote

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.logCacheMisses
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

object ApolloService {
    private val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory("apollo.db")
    private val cacheFactory =
        MemoryCacheFactory(10 * 1024 * 1024, expireAfterMillis = 60000) //10MB of cache , TTL=1 min

    fun setUpApolloClient(): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient.Builder().addInterceptor(logging)
        return ApolloClient.Builder().serverUrl("https://gitlab.com/api/graphql")
            .logCacheMisses({ res -> Log.w("CacheMiss", "object wasn't found in Cache${res}") })
            .normalizedCache(
                cacheFactory
            ).okHttpClient(okHttp.addInterceptor(TokenInterceptor()).build()).build()
    }

}
