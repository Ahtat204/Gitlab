package com.ahtat204.gitlab.data.helpers

import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Merges a new page of contributed projects into the existing cached list and updates the Apollo store.
 *
 * This function is intended to be used as an operator on a [Flow] that emits new  projects.
 * It performs a [FetchPolicy.CacheOnly] query to get the previous items, appends the new ones,
 * and writes the combined result back to the cache.
 *
 * @receiver A [Flow] emitting the latest [GetMyContributedProjectsQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param cursor The pagination cursor (optional). If null, this might represent the first page or a refresh.
 * @return A [Flow] emitting the merged [GetMyContributedProjectsQuery.Data].
 * @throws Exception Propagates any errors encountered during the cache read/write or flow collection.
 */
suspend fun Flow<GetMyContributedProjectsQuery.Data>.fetchAndMergeContributedProjects(
    client: ApolloClient, cursor: String? = null
): Flow<GetMyContributedProjectsQuery.Data> {
    if (cursor == null) return this
    try {
        val query = GetMyContributedProjectsQuery()
        var cachedData = client.query(
            query
        ).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val currentUser = cachedData.currentUser ?: return this
        var projects = currentUser.contributedProjects
            ?: return this //edge case , like the cache is got evacuated by the OS
        val nodes = projects.nodes.orEmpty()
        val newProjects = this.first().currentUser?.contributedProjects
        val newNodes = newProjects?.nodes
        val newPage = newProjects!!.pageInfo
        if (newNodes.isNullOrEmpty()) return this
        val totalProjects = nodes + newNodes
        projects = projects.copy(nodes = totalProjects, pageInfo = newPage)
        cachedData = GetMyContributedProjectsQuery.Data(
            currentUser.copy(
                contributedProjects = projects
            )
        )
        client.apolloStore.writeOperation(operation = query, publish = true, data = cachedData)
            .also { keys ->
                client.apolloStore.publish(keys)
            }
        return this
    } catch (e: Throwable) {
        throw e
    }
}