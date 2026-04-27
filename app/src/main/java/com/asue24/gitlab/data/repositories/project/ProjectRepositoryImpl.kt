package com.asue24.gitlab.data.repositories.project

import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetMyProjectsQuery.Data
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.GetRepoTreeQuery.Node
import com.asue24.gitlab.data.remote.ApolloService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.internal.notify

class ProjectRepositoryImpl() : ProjectRepository {
    private val gitlab = ApolloService.setUpApolloClient()

    /**
     * @brief Streams contributed projects from GitLab.
     * Uses context preservation and structured concurrency.
     */
    override suspend fun getAllProjects(): Flow<Data> {
        val result = gitlab.query(GetMyProjectsQuery()).toFlow()
        val response = result.map { resp ->
            if (resp.hasErrors()) {
                throw RuntimeException("GraphQL Errors: ${resp.errors}")
            }
            resp.dataAssertNoErrors
        }

        return response.flowOn(Dispatchers.IO)
    }

    override suspend fun getProjectById(id: String, path: String): Node {
        val scope= CoroutineScope(Dispatchers.IO)
        scope.async {
            val result = gitlab.query(GetRepoTreeQuery(id, path)).notify()

        }
    }

}