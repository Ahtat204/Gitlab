package com.ahtat204.gitlab.data.repositories.profile

import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) : ProfileRepository {
    override fun getMyProfile(): Flow<GetMyProfileQuery.Data> {
        val result = apolloClient.query(GetMyProfileQuery()).toFlow()
        val response = result.map { res ->
            if (res.hasErrors()) {
                throw RuntimeException("GraphQL Errors: ${res.errors}")
            }
            res.dataAssertNoErrors
        }
        return response.flowOn(Dispatchers.IO)
    }
}
