package com.ahtat204.gitlab.data

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

suspend fun Flow<GetRepositoryCommitsQuery.Data>.fetchAndMergeCommits(
    client: ApolloClient, branch: String, id: String, cursor: String? = null
): Flow<GetRepositoryCommitsQuery.Data> {
    try {
        val query = GetRepositoryCommitsQuery(
            projectPath = id, branch = branch
        )
        val cachedList = client.query(
            query
        ).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedList.project
        val repository = project?.repository
        val commits = repository?.commits
        val newPage = this.first().project?.repository?.commits
        val page = newPage!!.pageInfo
        val newList = newPage.nodes
        val mergedList = commits!!.nodes!!.toMutableList()
        newList?.forEach { node ->
            mergedList += node
        }
        if (mergedList?.isNotEmpty() == true) {
            val totalCommits = commits.copy(nodes = mergedList, pageInfo = page)
            val newData = GetRepositoryCommitsQuery.Data(
                project.copy(
                    repository = repository.copy(commits = totalCommits)
                )
            )
            client.apolloStore.writeOperation(
                operation = query, publish = true, data = newData
            ).also { keys ->
                client.apolloStore.publish(keys)
            }
            return flowOf(newData)
        } else return flowOf(cachedList)
    } catch (e: Exception) {
    throw e
    //Log.e("FetchAndMergeCommits", "${e.cause} \n ${e.message}}")
    }
  //  return flowOf()
}