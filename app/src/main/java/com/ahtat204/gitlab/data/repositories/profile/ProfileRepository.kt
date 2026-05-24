package com.ahtat204.gitlab.data.repositories.profile

import com.asue24.gitlab.data.queries.GetMyProfileQuery
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getMyProfile(): Flow<GetMyProfileQuery.Data>
}