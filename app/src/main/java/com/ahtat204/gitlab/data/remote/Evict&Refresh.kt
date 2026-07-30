package com.ahtat204.gitlab.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.removeOperation
import kotlinx.coroutines.flow.Flow

suspend fun <D : Operation.Data> ApolloClient.evictAndRefresh(
    query: Operation<D>, data: D, call: suspend () -> Flow<D>
) {
    this.apolloStore.removeOperation(query, data, publish = true)
    call()
}