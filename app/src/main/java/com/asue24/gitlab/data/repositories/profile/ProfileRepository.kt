package com.asue24.gitlab.data.repositories.profile

import com.asue24.gitlab.GetMyProfileQuery
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getMyProfile(): Flow<GetMyProfileQuery.Data>
}