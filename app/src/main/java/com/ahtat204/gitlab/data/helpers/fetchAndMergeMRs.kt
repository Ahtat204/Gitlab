package com.ahtat204.gitlab.data.helpers

import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Data
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Merges a new page of project merge requests into the existing cached list and updates the Apollo store.
 *
 * Similar to [com.ahtat204.gitlab.data.helpers.fetchAndMergeMembers], this function handles the manual merging logic for merge requests.
 * It ensures that pagination doesn't overwrite previously loaded merge requests in the UI.
 *
 * @receiver A [Flow] emitting the latest [com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param id The project identifier or full path.
 * @param cursor The pagination cursor. If null, the function returns the original flow (base case).
 * @return A [Flow] emitting the merged [com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery.Data].
 * @throws Throwable Propagates any errors encountered during the process.
 */
suspend fun Flow<Data>.fetchAndMergeMRs(
    client: ApolloClient,
    id: String,
    cursor: String? = null,
): Flow<Data> {
    if (cursor == null) return this
    try {
        val query = GetProjectMergeRequestsQuery(
            id
        )
        val cachedList =
            client.query(query).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedList.project
        val mrs = project?.mergeRequests
        val cachedMrs = cachedList.project?.mergeRequests?.nodes!!.toMutableList()
        val newMembers = this.first().project?.mergeRequests ?: return this
        val newNodes = newMembers.nodes!!
        val newPage = newMembers.pageInfo
        newNodes.forEach { node ->
            cachedMrs += node
        }
        val totalMembers = mrs.copy(nodes = cachedMrs, pageInfo = newPage)
        val newData = Data(
            project = project.copy(
                id = project.id, mergeRequests = totalMembers
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