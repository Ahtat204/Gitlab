package com.asue24.gitlab.data.repositories.project

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import com.apollographql.apollo.exception.CacheMissException
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
  override suspend fun getAllProjects(policy: FetchPolicy): Flow<GetMyProjectsQuery.Data> {
      val result= apolloClient.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull { it.data }.catch { ex->
          Log.e("errorhap",ex.cause.toString()+"\n"+ex.stackTrace)
      }.mapNotNull { it }
        return result

}





    override suspend fun getProjectById(id: String): Flow<GetRepoTreeQuery.Data?> {
            return flow {
        // Directly call the suspend function — no async needed
        val cacheResult = apolloClient.query(GetRepoTreeQuery(id))
            .fetchPolicy(FetchPolicy.CacheFirst)
            .execute()
            .dataAssertNoErrors

        emit(cacheResult)
    }.catch { ex ->
        if (ex is CancellationException) throw ex

        if (ex is CacheMissException) {

            val networkResult = apolloClient.query(GetRepoTreeQuery(id))
                .fetchPolicy(FetchPolicy.NetworkFirst)
                .execute()
                .data ?: throw IllegalStateException("No data returned from network")

            emit(networkResult)
        } else {
            throw ex
        }
    }
    }

}