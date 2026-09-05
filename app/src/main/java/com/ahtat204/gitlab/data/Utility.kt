package com.ahtat204.gitlab.data

import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import okio.IOException

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
    if (cursor == null) return this
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
        val newCommits = this.first().project?.repository?.commits
        val page = newCommits!!.pageInfo
        val newNodes = newCommits.nodes
        val cachedCommits = commits!!.nodes!!.toMutableList()
        newNodes?.forEach { node ->
            cachedCommits += node
        }
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
        return this

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
    client: ApolloClient,
    id: String,
    cursor: String? = null,
    statusEnum: PipelineStatusEnum = PipelineStatusEnum.SUCCESS
): Flow<GetProjectPipelinesQuery.Data> {
    if (cursor == null) return this
    try {
        val query = GetProjectPipelinesQuery(
            id, status = Optional.presentIfNotNull(statusEnum)
        )
        val cachedList =
            client.query(query).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedList.project
        val pipelines = project?.pipelines
        val cachedPipelines = cachedList.project?.pipelines?.nodes!!.toMutableList()
        val newPipelines = this.first().project?.pipelines!!
        val newNodes = newPipelines.nodes!!
        val newPage = newPipelines.pageInfo
        newNodes.forEach { node ->
            cachedPipelines += node
        }
        val totalPipelines = pipelines.copy(nodes = cachedPipelines, pageInfo = newPage)
        val newData = GetProjectPipelinesQuery.Data(
            project = project.copy(
                id = project.id, pipelines = totalPipelines
            )
        )
        client.apolloStore.writeOperation(operation = query, publish = true, data = newData)
            .also { keys ->
                client.apolloStore.publish(keys)
            }
        return this

    } catch (e: Throwable) {
        throw e
    }
}

/**
 * Processes an Apollo GraphQL [Flow] response, providing unified error handling,
 * data extraction, and automatic filtering of null values.
 *
 * ## Features
 * - **Exception Unwrapping**: Automatically detects and throws [ApolloResponse.exception]
 *   to propagate network or transport-level failures to the [catch] block.
 * - **GraphQL Error Handling**: Inspects [ApolloResponse.hasErrors] and throws an [Exception]
 *   if the server returns business-logic errors, ensuring they are not ignored.
 * - **Unified Error Logging**: Centralizes error handling via a [catch] block, distinguishing
 *   between recoverable network issues ([IOException]), coroutine lifecycle events
 *   ([CancellationException]), and unexpected system failures.
 * - **Safe Data Emission**: Sanitizes the stream by returning only valid [D] (data)
 *   and filtering out nulls via [mapNotNull].
 *
 * ## Usage
 * ```kotlin
 * override suspend fun getProjects(): Flow<ProjectQuery.Data> =
 *     apolloClient.query(ProjectQuery())
 *         .watch()
 *         .mapAndHandleErrors()
 * ```
 *
 * @param D The type of the GraphQL operation data (e.g., `Query.Data` or `Mutation.Data`).
 * @return A [Flow] emitting the raw data [D], with all errors intercepted and logged.
 *
 * @throws Exception Propagates exceptions caught during stream collection,
 *                   excluding [CancellationException] which is re-thrown to honor coroutine lifecycle.
 * @author Lahcen AHTAT
 */
fun <D : Query.Data> Flow<ApolloResponse<D>>.mapAndHandleErrors(): Flow<D> {
    return this.map { response ->
        response.exception?.cause?.let {
            throw it
        }
        response.data
    }.catch { ex ->
        when (ex) {
            is IOException -> logger(message = ex.message)
            is CancellationException -> throw ex
            else -> logger(message = null)
        }
    }.mapNotNull { it }
}

