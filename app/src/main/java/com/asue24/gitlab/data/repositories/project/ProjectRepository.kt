package com.asue24.gitlab.data.repositories.project

import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getAllProjects(): GetMyProjectsQuery.Data
    suspend fun getProjectById(id: String):Flow< GetRepoTreeQuery.Data?>
}