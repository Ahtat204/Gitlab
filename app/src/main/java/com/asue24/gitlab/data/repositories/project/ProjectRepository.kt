package com.asue24.gitlab.data.repositories.project

import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetProjectDetailsQuery
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getAllProjects(policy: FetchPolicy): Flow<GetMyProjectsQuery.Data>
    suspend fun getProjectById(id: String,policy: FetchPolicy): Flow<GetProjectDetailsQuery.Data?>
}