package com.asue24.gitlab.data.repositories.project

import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.data.remote.ApolloService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * this is a singleton object , which guarantees the ConcurrentHashMap will live throughout the Application lifecycle
 */
class ProjectRepositoryImpl() : ProjectRepository {
    private val gitlab = ApolloService.setUpApolloClient()

    /**
     * @brief Streams contributed projects from GitLab.
     * Uses context preservation and structured concurrency.
     */
    override fun getAllProjects(): Flow<GetMyProjectsQuery.Data> {
        val result = gitlab.query(GetMyProjectsQuery()).toFlow()
        val response = result.map { resp ->
            if (resp.hasErrors()) {
                throw RuntimeException("GraphQL Errors: ${resp.errors}")
            }
            resp.dataAssertNoErrors
        }

        return response.flowOn(Dispatchers.IO)
    }

    override suspend fun getProjectById(id: String, path: String): GetRepoTreeQuery.Project? =
        gitlab.query(GetRepoTreeQuery(id, path)).execute().dataAssertNoErrors.project

}
