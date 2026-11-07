package com.ahtat204.gitlab.data.helpers

import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery.Data
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


/**
 * Merges a new page of project members into the existing cached list and updates the Apollo store.
 *
 * Similar to [com.ahtat204.gitlab.data.helpers.fetchAndMergeCommits], this function handles the manual merging logic for members.
 * It ensures that pagination doesn't overwrite previously loaded members in the UI.
 *
 * @receiver A [Flow] emitting the latest [GetProjectMembersQuery.Data] (the new page).
 * @param client The [ApolloClient] instance providing access to the [apolloStore].
 * @param id The project identifier or full path.
 * @param cursor The pagination cursor. If null, the function returns the original flow (base case).
 * @return A [Flow] emitting the merged [GetProjectMembersQuery.Data].
 * @throws Throwable Propagates any errors encountered during the process.
 */
suspend fun Flow<Data>.fetchAndMergeMembers(
    client: ApolloClient,
    id: String,
    cursor: String? = null,
): Flow<Data> {
    if (cursor == null) return this
    try {
        val query = GetProjectMembersQuery(
            id
        )
        val cachedList =
            client.query(query).fetchPolicy(FetchPolicy.CacheOnly).execute().dataAssertNoErrors
        val project = cachedList.project
        val members = project?.projectMembers
        val cachedMembers = cachedList.project?.projectMembers?.nodes!!.toMutableList()
        val newMembers = this.first().project?.projectMembers!!
        val newNodes = newMembers.nodes!!
        val newPage = newMembers.pageInfo
        newNodes.forEach { node ->
            cachedMembers += node
        }
        val totalMembers = members.copy(nodes = cachedMembers, pageInfo = newPage)
        val newData = GetProjectMembersQuery.Data(
            project = project.copy(
                id = project.id, projectMembers = totalMembers
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