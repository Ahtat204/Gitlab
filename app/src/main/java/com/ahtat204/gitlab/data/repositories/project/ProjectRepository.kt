package com.ahtat204.gitlab.data.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.apollographql.apollo.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    /**
     * Streams all projects the authenticated user has contributed to.
     * @return A [Flow] emitting [GetMyProjectsPaginatedQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetMyProjectsPaginatedQuery] with the provided fetch policy.
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
    suspend fun getAllProjects(): Flow<GetMyProjectsPaginatedQuery.Data>

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
    suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?>
    suspend fun getProjectCommits(id: String,cursor:String?): Flow<GetProjectCommitsQuery.Data?>

}