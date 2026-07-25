package com.ahtat204.gitlab.data

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.Query.Data
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

/**
 * Utility functions for manually merging paginated GraphQL response data into the Apollo Normalized Cache.
 *
 * ## Overview
 * Apollo’s default caching mechanism for paginated fields often replaces the existing list with the new page.
 * These extension functions provide a way to:
 * 1. Retrieve the currently cached list of items (e.g., Commits or Pipelines).
 * 2. Collect the new page of data from a [Flow].
 * 3. Merge the new items into the cached collection.
 * 4. Manually write the merged dataset back to the [com.apollographql.cache.normalized.ApolloStore].
 * 5. Publish the changes to update active watchers (via [com.apollographql.cache.normalized.watch]).
 *
 * @author Lahcen AHTAT
 */
/**
 * Merges a new page of repository commits into the existing cached list and updates the Apollo store.
 *
 * This function is intended to be used as an operator on a [Flow] that emits new commit data.
 * It performs a [FetchPolicy.CacheOnly] query to get the previous items, appends the new ones,
 * and writes the combined result back to the cache.
 *
 * @receiver A [Flow] emitting the latest [GetRepositoryCommitsQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param branch The git branch reference used to identify the query in the cache.
 * @param id The project identifier or full path.
 * @param cursor The pagination cursor (optional). If null, this might represent the first page or a refresh.
 * @return A [Flow] emitting the merged [GetRepositoryCommitsQuery.Data].
 * @throws Exception Propagates any errors encountered during the cache read/write or flow collection.
 */
suspend fun Flow<GetRepositoryCommitsQuery.Data>.fetchAndMergeCommits(
    client: ApolloClient, branch: String, id: String, cursor: String? = null
): Flow<GetRepositoryCommitsQuery.Data> {
    try {
        val cache = client.apolloStore.dump().toString()
        Log.d("com.ahtat204.gitlab.logger", cache ?: "an error occurred")
        val query = GetRepositoryCommitsQuery(
            projectPath = id, branch = branch
        )
        val cachedList = client.query(
            query
        ).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedList.project
        val repository = project?.repository
        val commits = repository?.commits
        val newCommits = this.first().project?.repository?.commits
        val page = newCommits!!.pageInfo
        val newNodes = newCommits.nodes
        val cachedCommits = commits!!.nodes!!.toMutableList()
        newNodes?.forEach { node ->
            cachedCommits += node
        }
        if (cachedCommits.isNotEmpty()) {
            val totalCommits = commits.copy(nodes = cachedCommits, pageInfo = page)
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
    }

}

/**
 * Merges a new page of project pipelines into the existing cached list and updates the Apollo store.
 *
 * Similar to [fetchAndMergeCommits], this function handles the manual merging logic for pipelines.
 * It ensures that pagination doesn't overwrite previously loaded pipelines in the UI.
 *
 * @receiver A [Flow] emitting the latest [GetProjectPipelinesQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param id The project identifier or full path.
 * @param cursor The pagination cursor. If null, the function returns the original flow (base case).
 * @param statusEnum Optional filter for pipeline status.
 * @return A [Flow] emitting the merged [GetProjectPipelinesQuery.Data].
 * @throws Throwable Propagates any errors encountered during the process.
 */
suspend fun Flow<GetProjectPipelinesQuery.Data>.fetchAndMergePipelines(
    client: ApolloClient, id: String, cursor: String? = null, statusEnum: PipelineStatusEnum? = null
): Flow<GetProjectPipelinesQuery.Data> {
    if (cursor == null) return this
    try {
        val query = GetProjectPipelinesQuery(
            id, Optional.presentIfNotNull(cursor), Optional.presentIfNotNull(statusEnum)
        )
        val cachedList =
            client.query(query).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedList.project
        val pipelines = project?.pipelines
        val cachedPipelines = cachedList.project?.pipelines?.nodes!!.toMutableList()
        val newPage = this.first().project?.pipelines!!
        val newPipelines = newPage.nodes!!
        newPipelines.forEach { node ->
            cachedPipelines += node
        }
        if (cachedPipelines.isNotEmpty()) {
            val totalPipelines =
                pipelines.copy(nodes = cachedPipelines, pageInfo = newPage.pageInfo)
            val newData = GetProjectPipelinesQuery.Data(
                project = project.copy(
                    id = project.id, pipelines = totalPipelines
                )
            )
            client.apolloStore.writeOperation(operation = query, publish = true, data = newData)
                .also { keys ->
                    client.apolloStore.publish(keys)
                }
            return flowOf(newData)
        } else return flowOf(cachedList)

    } catch (e: Throwable) {
        throw e
    }
}

suspend fun <D : Data> Flow<ApolloResponse<GetProjectPipelinesQuery.Data>>.fetchAndMerge(
    client: ApolloClient, id: String, cursor: String? = null, statusEnum: PipelineStatusEnum? = null
): Flow<ApolloResponse<GetProjectPipelinesQuery.Data>> {
    if (cursor == null) return this
    try {
        val query = GetProjectPipelinesQuery(
            id, Optional.presentIfNotNull(cursor), Optional.presentIfNotNull(statusEnum)
        )
        val cachedList = client.query(query).fetchPolicy(FetchPolicy.CacheOnly).execute().data
        val project = cachedList?.project
        val pipelines = project?.pipelines
        val response = this.first()
        val cachedPipelines = cachedList?.project?.pipelines?.nodes?.toMutableList()
        val newPage = response.data?.project?.pipelines
        val newPipelines = newPage?.nodes
        newPipelines?.forEach { node ->
            cachedPipelines?.plusAssign(node)
        }
        if (newPipelines?.isNotEmpty() == true) {
            val totalPipelines =
                pipelines?.copy(nodes = cachedPipelines, pageInfo = newPage.pageInfo)
            val newData = GetProjectPipelinesQuery.Data(
                project = project?.copy(
                    id = project.id, pipelines = totalPipelines
                )
            )
            client.apolloStore.writeOperation(operation = query, publish = true, data = newData)
                .also { keys ->
                    client.apolloStore.publish(keys)
                }

            return flowOf(
                ApolloResponse.Builder(
                    requestUuid = response.requestUuid,
                    operation = response.operation,
                ).data(newData).build()
            )
        } else return flowOf(
            ApolloResponse.Builder(
                requestUuid = response.requestUuid,
                operation = response.operation,
            ).data(cachedList).build()
        )

    } catch (e: Throwable) {
        throw e
    }

}

suspend fun <D : Data> Flow<ApolloResponse<GetRepositoryCommitsQuery.Data>>.fetchAndMerge(
    client: ApolloClient, branch: String, id: String, cursor: String? = null
): Flow<ApolloResponse<GetRepositoryCommitsQuery.Data>> {
    if (cursor == null) return this
    val cache = client.apolloStore.dump().forEach { klass, map ->
        map.forEach { (key, record) ->
            Log.d(key.key, record.toString())
        }
    }
    val query = GetRepositoryCommitsQuery(
        projectPath = id, branch = branch
    )
    val cachedList = client.query(
        query
    ).fetchPolicy(FetchPolicy.CacheOnly).execute().data
    val project = cachedList?.project
    val repository = project?.repository
    val commits = repository?.commits
    val result = CompletableDeferred<ApolloResponse<GetRepositoryCommitsQuery.Data>?>(null)
    this.collect { result.complete(it) }
    val response=result.await()
    if (response is ApolloResponse<GetRepositoryCommitsQuery.Data>) {
        val newCommits = response.data?.project?.repository?.commits
        val page = newCommits!!.pageInfo
        val newNodes = newCommits.nodes
        val cachedCommits = commits?.nodes?.toMutableList()
        if (newNodes?.isNotEmpty() == true) {
            val totalCommits = commits?.copy(nodes = cachedCommits, pageInfo = page)
            val newData = GetRepositoryCommitsQuery.Data(
                project?.copy(
                    repository = repository?.copy(commits = totalCommits)
                )
            )
            client.apolloStore.writeOperation(
                operation = query, publish = true, data = newData
            ).also { keys ->
                client.apolloStore.publish(keys)
            }

            return flowOf(
                ApolloResponse.Builder(
                    requestUuid = response.requestUuid,
                    operation = response.operation,
                ).data(newData).build()
            )
        } else return flowOf(
            ApolloResponse.Builder(
                requestUuid = response.requestUuid,
                operation = response.operation,
            ).data(cachedList).build()
        )
    } else {
        Log.e("com.ahtat204.gitlab.logger", "data is null")
        return this
    }

}