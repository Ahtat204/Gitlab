package com.asue24.gitlab.data.repositories.project

import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.data.remote.ApolloService
import kotlinx.coroutines.flow.Flow

class ProjectRepositoryImpl():ProjectRepository {
    private val apolloClient = ApolloService.setUpApolloClient()
    override fun getAllProjects(): Flow<GetMyProjectsQuery.ContributedProjects> {
        TODO("Not yet implemented")
    }
    override fun getProjectById(id: String): GetMyProjectsQuery.ContributedProjects {
        TODO("Not yet implemented")
    }

}