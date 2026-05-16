package com.asue24.gitlab.data.repositories.project

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import com.asue24.gitlab.data.queries.GetMyProjectsQuery
import com.asue24.gitlab.data.queries.GetProjectDetailsQuery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * Implementation of [ProjectRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Overview
 * - Provides reactive streams of project data using Kotlin [Flow].
 * - Uses Apollo’s normalized caching with configurable [FetchPolicy].
 * - Annotated with `@Inject` for dependency injection, ensuring a singleton lifecycle.
 *
 * ## Responsibilities
 * - Fetch all projects contributed by the authenticated user.
 * - Retrieve repository tree data for a specific project by ID.
 * - Handle errors gracefully with logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * - [GetMyProjectsQuery], [GetProjectDetailsQuery]: Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 */
class ProjectRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProjectRepository {
    /**
     * Streams all projects the authenticated user has contributed to.
     *
     * @param policy The [FetchPolicy] to control cache vs. network behavior.
     * @return A [Flow] emitting [GetMyProjectsQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetMyProjectsQuery] with the provided fetch policy.
     * - Uses Apollo’s `watch()` to continuously observe changes.
     * - Filters out null results with `mapNotNull`.
     * - Logs exceptions with [Log.e] while keeping the stream alive.
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getAllProjects(FetchPolicy.CacheFirst)
     *         .collect { projects -> renderProjects(projects) }
     * }
     * ```
     */
    @OptIn(ApolloExperimental::class)
    override suspend fun getAllProjects(policy: FetchPolicy): Flow<GetMyProjectsQuery.Data> {
        return apolloClient.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).watch()
            .mapNotNull { it.data }.catch { ex ->
                Log.e("ProjectRepository", ex.cause.toString() + "\n" + ex.stackTrace)
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }

    /**
     * Retrieves the repository tree for a given project.
     *
     * @param id The unique identifier of the project.
     * @return A [Flow] emitting [GetProjectDetailsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectDetailsQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Logs errors without terminating the stream.
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getProjectById("12345")
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
     */
    override suspend fun getProjectById(
        id: String,
        policy: FetchPolicy
    ): Flow<GetProjectDetailsQuery.Data?> {
        return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(policy).watch()
            .mapNotNull { it.data }.catch { ex ->
                Log.e("ProjectRepository", ex.cause.toString() + "\n" + ex.stackTrace)
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }
}
