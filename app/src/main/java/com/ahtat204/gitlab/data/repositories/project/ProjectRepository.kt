package com.ahtat204.gitlab.data.repositories.project

import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.ahtat204.gitlab.data.queries.GetMyProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getAllProjects(policy: FetchPolicy): Flow<GetMyProjectsQuery.Data>
    suspend fun getProjectById(id: String, policy: FetchPolicy): Flow<GetProjectDetailsQuery.Data?>

}