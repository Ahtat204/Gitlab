package com.ahtat204.gitlab.data.remote.repositories.profile

import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.apollographql.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getMyProfile(): Flow<GetMyProfileQuery.Data>
}