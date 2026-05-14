package com.asue24.gitlab.data.repositories.project

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.apolloStore
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.exception.CacheMissException
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * this is a singleton object , which guarantees the ConcurrentHashMap will live throughout the Application lifecycle
 */
class ProjectRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) :
    ProjectRepository {
    /**
     * @brief Streams contributed projects from GitLab.
     * Uses context preservation and structured concurrency.
     */
    override suspend fun getAllProjects(): Flow<GetMyProjectsQuery.Data> {
        return flow {
          emit(apolloClient.apolloStore.readOperation(GetMyProjectsQuery()))
        }.catch { ex ->
            if (ex is CacheMissException) {
               emit( apolloClient.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).execute().data!!)
            }
        }
    }

    override suspend fun getProjectById(id: String): Flow<GetRepoTreeQuery.Data?> {
        val result =
            apolloClient.query(GetRepoTreeQuery(id)).fetchPolicy(FetchPolicy.CacheFirst).toFlow()
        val response = result.map { data ->
            if (data.hasErrors()) {
                Log.e("GraphQL Error", data.exception.toString())
            }
            data.dataAssertNoErrors
        }
        return response.flowOn(Dispatchers.IO)
    }

}