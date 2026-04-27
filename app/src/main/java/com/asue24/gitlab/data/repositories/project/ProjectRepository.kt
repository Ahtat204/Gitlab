package com.asue24.gitlab.data.repositories.project

import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery.Node
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getAllProjects(): Flow<GetMyProjectsQuery.Data>
    suspend fun getProjectById(id: String, path: String): Node
}