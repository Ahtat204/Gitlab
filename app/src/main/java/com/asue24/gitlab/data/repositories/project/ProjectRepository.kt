package com.asue24.gitlab.data.repositories.project

import com.asue24.gitlab.GetMyProjectsQuery
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<GetMyProjectsQuery.ContributedProjects>
    fun getProjectById(id:String):GetMyProjectsQuery.ContributedProjects
}