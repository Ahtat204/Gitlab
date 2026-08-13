package com.ahtat204.gitlab.data

import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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
