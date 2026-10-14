package com.ahtat204.gitlab.data.helpers

import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Merges a new page of project pipelines into the existing cached list and updates the Apollo store.
 *
 * Similar to [com.ahtat204.gitlab.data.helpers.fetchAndMergeCommits], this function handles the manual merging logic for pipelines.
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
