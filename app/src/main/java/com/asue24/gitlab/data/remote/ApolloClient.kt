package com.asue24.gitlab.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.apollographql.apollo.network.okHttpClient
import com.asue24.gitlab.domain.authentication.constants.Tokens
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApolloService {
    private val sqlNormalizedCacheFactory = SqlNormalizedCacheFactory("apollo.db")

    private val cacheFactory = MemoryCacheFactory(10 * 1024 * 1024, expireAfterMillis = 60000)
        .chain(sqlNormalizedCacheFactory) //10MB of cache , TTL=1 min
    public fun setUpApolloClient(): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient.Builder().addInterceptor(logging)
        return ApolloClient.Builder().serverUrl("https://gitlab.com/api/graphql").normalizedCache(
            cacheFactory
        ).addHttpInterceptor(object : HttpInterceptor {
                override suspend fun intercept(
                    request: HttpRequest, chain: HttpInterceptorChain
                ): HttpResponse {
                    return chain.proceed(
                        request.newBuilder()
                            .addHeader("Authorization", "Bearer ${Tokens.accessToken}").build()
                    )
                }
            }).okHttpClient(okHttp.addInterceptor(TokenInterceptor()).build()).build()
    }
}
