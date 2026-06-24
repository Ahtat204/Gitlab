package com.ahtat204.gitlab.domain.usecase.pagination

import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.apolloStore

suspend fun mergeCommits(
    apollo: ApolloClient,
    query: GetRepositoryCommitsQuery,
    incoming: GetRepositoryCommitsQuery.Data?
) {
    val existing = apollo.apolloStore.readOperation(query)
    val mergedNodes = (existing.project?.repository?.commits?.nodes
        ?: emptyList()) + (incoming?.project?.repository?.commits?.nodes ?: emptyList())
    val updated = existing.project?.let { project ->
        project.copy(
            repository = project.repository?.let { repo ->
                repo.copy(
                    commits = repo.commits?.copy(
                        nodes = mergedNodes
                    )
                )
            })
    }
    apollo.apolloStore.writeOperation(
        query, existing.copy(project = updated)
    )
}
