package com.ahtat204.gitlab.data.remote.repositories.user

import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProjectsByName(
        userName: String
    ): Flow<GetUserProjectsByNameQuery.Data?>
}