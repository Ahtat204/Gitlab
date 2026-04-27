package com.asue24.gitlab.data.repositories.profile

import com.asue24.gitlab.GetMyProfileQuery
import com.asue24.gitlab.data.remote.ApolloService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl : ProfileRepository {
    private val gitlab = ApolloService.setUpApolloClient()
    override fun getMyProfile(): Flow<GetMyProfileQuery.Data> {
        val result = gitlab.query(GetMyProfileQuery()).toFlow()
        val response = result.map { res ->
            if (res.hasErrors()) {
                throw RuntimeException("GraphQL Errors: ${res.errors}")
            }
            res.dataAssertNoErrors
        }
        return response.flowOn(Dispatchers.IO)
    }
}
