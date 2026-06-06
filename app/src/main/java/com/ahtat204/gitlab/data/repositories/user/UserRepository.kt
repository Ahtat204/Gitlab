package com.ahtat204.gitlab.data.repositories.user

import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import com.apollographql.apollo.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProjectsByName(userName: String,policy: FetchPolicy):Flow<GetUserProjectsByNameQuery.Data?>
}