package com.ahtat204.gitlab.data.helpers

import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Merges a new page of project issues into the existing cached list and updates the Apollo store.
 *
 * Similar to [fetchAndMergeCommits], this function handles the manual merging logic for issues.
 * It ensures that pagination doesn't overwrite previously loaded issues in the UI.
 *
 * @receiver A [Flow] emitting the latest [GetProjectIssuesQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param id The project identifier or full path.
 * @param cursor The pagination cursor. If null, the function returns the original flow (base case).
 * @return A [Flow] emitting the merged [GetProjectIssuesQuery.Data].
 * @throws Throwable Propagates any errors encountered during the process.
 */
suspend fun Flow<GetProjectIssuesQuery.Data>.fetchAndMergeIssues(
    id: String,
    client: ApolloClient, cursor: String? = null
): Flow<GetProjectIssuesQuery.Data> {
    if (cursor == null) return this
    try {
        val query = GetProjectIssuesQuery(id)
        var cachedData = client.query(
            query
        ).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedData.project ?: return this
        var issues = project.issues ?: return this
        val nodes = issues.nodes.orEmpty()
        val newIssues = this.first().project?.issues
        val newNodes = newIssues?.nodes
        val newPage = newIssues!!.pageInfo
        if (newNodes.isNullOrEmpty()) return this
        val totalNodes = nodes + newNodes
        issues = issues.copy(nodes = totalNodes, pageInfo = newPage)
        cachedData = GetProjectIssuesQuery.Data(
            project.copy(
                issues = issues
            )
        )
        client.apolloStore.writeOperation(operation = query, publish = true, data = cachedData)
            .also { keys ->
                client.apolloStore.publish(keys)
            }
        return this
    } catch (e: Exception) {
        throw e
    }
}