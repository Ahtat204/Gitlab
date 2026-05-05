package com.asue24.gitlab.data.remote

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.logCacheMisses
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.network.okHttpClient
import com.asue24.gitlab.domain.authentication.constants.Tokens
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApolloService {
    private val cacheFactory = MemoryCacheFactory(10 * 1024 * 1024, expireAfterMillis = 600000)
    val client: ApolloClient by lazy {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }).addInterceptor(AuthenticationInterceptor()).build()

        ApolloClient.Builder().serverUrl("https://gitlab.com/api/graphql").addHttpHeader("Authorization", "Bearer ${Tokens.accessToken}")
            .okHttpClient(okHttpClient)
            .logCacheMisses({ res -> Log.w("CacheMiss", "object wasn't found in Cache${res}") })
            .normalizedCache(cacheFactory, writeToCacheAsynchronously = false).build()
    }
}
