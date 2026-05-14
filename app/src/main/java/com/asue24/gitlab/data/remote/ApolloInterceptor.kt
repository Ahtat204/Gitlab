package com.asue24.gitlab.data.remote

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

object CacheInterceptor: ApolloInterceptor {
    override fun <D : Operation.Data> intercept(
        request: ApolloRequest<D>, chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> {
         return flow {
      val cacheResponse = chain.proceed(request = request.newBuilder().fetchPolicy(FetchPolicy.CacheOnly).build()).single()
      val fetchFromNetwork = cacheResponse.exception != null || cacheResponse.errors != null && cacheResponse.errors!!.isNotEmpty()
      emit(cacheResponse.newBuilder().isLast(!fetchFromNetwork).build())
      if (fetchFromNetwork) {
        emitAll(chain.proceed(request = request))
      }
    }
  }
    }
