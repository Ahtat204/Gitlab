package com.asue24.gitlab.data.remote

import android.os.Environment
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.http.httpCache
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

object ApolloService {
    private val cacheFactory = MemoryCacheFactory(10 * 1024 * 1024, expireAfterMillis = 600000)
    val client: ApolloClient by lazy {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }).addInterceptor(AuthenticationInterceptor()).build()

        ApolloClient.Builder().httpCache(
      directory =File(Environment. getExternalStorageDirectory().path+"gitlab/httpCache"),
      maxSize = 100 * 1024 * 1024
    ).serverUrl("https://gitlab.com/api/graphql")
            .normalizedCache(cacheFactory, writeToCacheAsynchronously = false)
            .okHttpClient(okHttpClient).fetchPolicy(FetchPolicy.CacheFirst).build()
    }
}
