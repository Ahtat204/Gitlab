package com.asue24.gitlab.data.repositories.project

import android.util.Log
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.exception.CacheMissException
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.data.remote.ApolloService
import com.asue24.gitlab.domain.authentication.constants.Tokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * this is a singleton object , which guarantees the ConcurrentHashMap will live throughout the Application lifecycle
 */
class ProjectRepositoryImpl : ProjectRepository {
    private val gitlab = ApolloService.client

    /**
     * @brief Streams contributed projects from GitLab.
     * Uses context preservation and structured concurrency.
     */
    override suspend fun getAllProjects(): GetMyProjectsQuery.Data {
       try{
           val result = gitlab.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).execute().dataAssertNoErrors
           return result
       }
       catch(ex:Exception){
           if(ex is CacheMissException){
               return gitlab.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.NetworkFirst).execute().dataAssertNoErrors
           }
       }
        return GetMyProjectsQuery.Data(null)
    }

    override suspend fun getProjectById(id: String): Flow<GetRepoTreeQuery.Data?> {
        val result = gitlab.query(GetRepoTreeQuery(id)).fetchPolicy(FetchPolicy.CacheFirst).toFlow()
        val response = result.map { data ->
            if (data.hasErrors()) {
                Log.e("GraphQL Error", data.exception.toString())
            }
            data.dataAssertNoErrors
        }
        return response.flowOn(Dispatchers.IO)
    }

}
