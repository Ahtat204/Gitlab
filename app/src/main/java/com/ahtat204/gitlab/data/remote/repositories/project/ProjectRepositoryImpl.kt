package com.ahtat204.gitlab.data.remote.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ProjectRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Overview
 * - Provides reactive streams of project data using Kotlin [Flow].
 * - Uses Apollo’s normalized caching with configurable [FetchPolicy].
 * - Annotated with `@Inject` for dependency injection, ensuring a singleton lifecycle.
 * - Annotated with [Singleton] to avoid creating a new Repository everytime the ViewModel is created since this Dependency is just for fetching data , doesn't have a state to hold
 *
 * ## Responsibilities
 * - Fetch all projects contributed by the authenticated user.
 * - Retrieve repository tree data for a specific project by ID.
 * - Handle errors gracefully with logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * - [GetMyProjectsPaginatedQuery], [GetProjectDetailsQuery],[GetRepositoryCommitsQuery],[GetProjectMergeRequestsQuery],[GetProjectRepositoryQuery]: Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 */
@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProjectRepository {
    @OptIn(ApolloExperimental::class)
    override suspend fun getAllProjects(): Flow<GetMyProjectsPaginatedQuery.Data> =
        apolloClient.query(GetMyProjectsPaginatedQuery()).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapNotNull { it.data }.catch { ex ->
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }

    override suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?> {
        return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapNotNull { it.data }.catch { ex ->
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }

    override suspend fun getProjectCommits(
        id: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?> {
        return if (cursor == null) apolloClient.query(GetRepositoryCommitsQuery(id))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull { it.data }.catch { ex ->
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
        else apolloClient.query(GetRepositoryCommitsQuery(id, Optional.Present(cursor)))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull {
                Log.d("PagingCursor", cursor)
                it.data
            }.catch { ex ->
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }

    override suspend fun getProjectMergeRequests(
        id: String, cursor: String?
    ): Flow<GetProjectMergeRequestsQuery.Data> {
        return if (cursor == null) apolloClient.query(GetProjectMergeRequestsQuery(id))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull { it.data }.catch { ex ->
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
        else apolloClient.query(GetProjectMergeRequestsQuery(id, Optional.Present(cursor)))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull {
                Log.d("PagingCursor", cursor)
                it.data
            }.catch { ex ->
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }

    override suspend fun getProjectRepository(
        id: String,
        skip: Int,
        branch: String?
    ): Flow<GetProjectRepositoryQuery.Data?> {
        return if (branch == null) {
            apolloClient.query(GetProjectRepositoryQuery(id, skip = skip))
                .fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull { it.data }.catch { ex ->
                    if (ex is CancellationException) throw ex
                }.mapNotNull { it }
        } else {
            apolloClient.query(
                GetProjectRepositoryQuery(
                    id,
                    skip = skip,
                    branch = Optional.present(branch)
                )
            ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapNotNull { it.data }.catch { ex ->
                    if (ex is CancellationException) throw ex
                }.mapNotNull { it }
        }
    }

}
