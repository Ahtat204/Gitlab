package com.ahtat204.gitlab.data

import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Merges a new page of personal projects into the existing cached list and updates the Apollo store.
 *
 * This function is intended to be used as an operator on a [Flow] that emits new  projects.
 * It performs a [FetchPolicy.CacheOnly] query to get the previous items, appends the new ones,
 * and writes the combined result back to the cache.
 *
 * @receiver A [Flow] emitting the latest [GetMyPersonalProjectsQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param cursor The pagination cursor (optional). If null, this might represent the first page or a refresh.
 * @return A [Flow] emitting the merged [GetMyPersonalProjectsQuery.Data].
 * @throws Exception Propagates any errors encountered during the cache read/write or flow collection.
 */
suspend fun Flow<GetMyPersonalProjectsQuery.Data>.fetchAndMergeProjects(
    client: ApolloClient, cursor: String? = null
): Flow<GetMyPersonalProjectsQuery.Data> {
    if (cursor == null) return this
    try {
        val query = GetMyPersonalProjectsQuery()
        var cachedData = client.query(
            query
        ).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val currentUser = cachedData.currentUser ?: return this
        val namespace = currentUser.namespace ?: return this
        var projects = namespace.projects
            ?: return this //edge case , like the cache is got evacuated by the OS
        val nodes = projects.nodes?.toMutableList()
        val newProjects = this.first().currentUser?.namespace?.projects
        val newNodes = newProjects?.nodes
        val newPage = newProjects!!.pageInfo
        if (newNodes.isNullOrEmpty()) return this

        newNodes.forEach { node ->
            nodes?.plusAssign(node)
        }
        projects = projects.copy(nodes = nodes, pageInfo = newPage)
        cachedData = GetMyPersonalProjectsQuery.Data(
            currentUser.copy(
                avatarUrl = currentUser.avatarUrl,
                id = currentUser.id,
                namespace = namespace.copy(projects = projects)
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