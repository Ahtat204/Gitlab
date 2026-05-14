package com.asue24.gitlab.data.repositories.project

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.apolloStore
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import com.apollographql.apollo.exception.CacheMissException
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.*
/**
 * this is a singleton object , which guarantees the ConcurrentHashMap will live throughout the Application lifecycle
 */
class ProjectRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) :
    ProjectRepository {
    /**
     * @brief Streams contributed projects from GitLab.
     * Uses context preservation and structured concurrency.
     */
    @OptIn(ApolloExperimental::class)
    override suspend fun getAllProjects(): Flow<GetMyProjectsQuery.Data> {

        return flow {
            val result:Deferred<GetMyProjectsQuery.Data> = CoroutineScope(Dispatchers.IO).async {
                apolloClient.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).execute().dataAssertNoErrors
            }
          emit(result.await())
        }.catch { ex ->
            if (ex is CacheMissException) {
               apolloClient.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.NetworkFirst).retryOnError
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