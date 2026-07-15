package com.ahtat204.gitlab.data.remote.repositories.profile

import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.apollographql.apollo.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data>
}