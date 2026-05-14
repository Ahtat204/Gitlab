package com.asue24.gitlab.domain.di

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.exception.CacheMissException
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.apollographql.apollo.network.okHttpClient
import com.asue24.gitlab.data.remote.AuthenticationInterceptor
import com.asue24.gitlab.domain.authentication.constants.Tokens
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onEach
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {
    private val cacheFactory = MemoryCacheFactory(10 * 1024 * 1024, expireAfterMillis = 60000)

    @Singleton
    @Provides
    fun GetApolloService(): ApolloClient {
        return ApolloClient.Builder().serverUrl("https://gitlab.com/api/graphql")
            .addHttpHeader("Authorization", "Bearer ${Tokens.accessToken}").okHttpClient(
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                }).addInterceptor(AuthenticationInterceptor()).build()
            ).normalizedCache(
                cacheFactory, writeToCacheAsynchronously = false
            ).addInterceptor(object : ApolloInterceptor {
                override fun <D : Operation.Data> intercept(
                    request: ApolloRequest<D>, chain: ApolloInterceptorChain
                ): Flow<ApolloResponse<D>> {
                    Log.d("interceptor", "Request started for: ${request.operation.name()}")

                    return chain.proceed(request).onEach { response ->
                        if (response.data == null && response.exception is CacheMissException) {
                            Log.d("interceptor", "Detected Cache Miss via Response Object")
                        }
                    }.catch { ex ->
                        Log.e("interceptor", "Caught exception: ${ex.javaClass.simpleName}")
                        if (ex == CacheMissException) {
                            Log.d("interceptor", "Pivoting to NetworkFirst...")
                            val newRequest =
                                request.newBuilder().fetchPolicy(FetchPolicy.NetworkFirst).build()
                            emitAll(chain.proceed(newRequest))
                        }
                    }
                }
            }).build()

    }
}