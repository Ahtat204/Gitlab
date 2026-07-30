package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.data.fetchAndMergeCommits
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.remote.repositories.mapAndHandleErrors
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.removeOperation
import com.apollographql.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ProjectRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Architecture & Data Strategy
 * This implementation serves as the **Single Source of Truth (SSOT)** for project-related data.
 * It sits between the domain layer (Use Cases/ViewModels) and GitLab's GraphQL API, providing 
 * reactive, cache-backed data streams that automatically update the UI when underlying data changes.
 *
 * ### Key Technologies
 * - **Apollo GraphQL**: Leveraged for structured data fetching and normalized caching.
 * - **Kotlin Coroutines Flow**: Provides asynchronous, cold streams for reactive data observation.
 * - **Normalized Cache**: Enables "Single Source of Truth" behavior across the entire application.
 *
 * ### Caching Policy
 * Most methods utilize [FetchPolicy.CacheFirst]. This ensures the UI remains responsive by 
 * emitting cached data immediately, while Apollo handles the network request in the background 
 * to refresh the cache and trigger subsequent emissions if data has changed.
 *
 * @param apolloClient The configured Apollo client used for executing GraphQL operations.
 * @author Lahcen AHTAT
 */
@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProjectRepository {
    /**
     * **Project Landscape Discovery**: Streams all projects where the authenticated user is a member.
     *
     * This is the primary entry point for the dashboard. It retrieves a comprehensive view of the 
     * user's project landscape, including topics, visibility levels, and recent activity timestamps.
     *
     * @return A [Flow] emitting the user's project memberships.
     * @see <a href="file:///app/src/main/graphql/com/ahtat204/user/currentUser/GetMyPersonalProjects.graphql">GetMyPersonalProjects.graphql</a>
     */
    @OptIn(ApolloExperimental::class)
    override suspend fun getAllProjects(): Flow<GetMyPersonalProjectsQuery.Data> =
        apolloClient.query(GetMyPersonalProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).watch()
            .mapAndHandleErrors()

    /**
     * **Project Intelligence**: Provides deep-dive metadata and analytics for a specific GitLab project.
     *
     * Beyond basic naming, this retrieves quantitative data like star counts, fork counts, 
     * and detailed issue metrics, enabling rich project overview screens.
     *
     * @param id The unique identifier (GID) or full path of the GitLab project.
     * @return A reactive stream of project metadata, or null if inaccessible.
     * @see <a href="file:///app/src/main/graphql/com/ahtat204/project/GetProjectDetails.graphql">GetProjectDetails.graphql</a>
     */
    override suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?> {
        return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapAndHandleErrors()
    }

    /**
     * Manually invalidates and refreshes data in the normalized cache for specific queries.
     *
     * Currently supports:
     * - [GetMyPersonalProjectsQuery.Data]: Removes the cached project list.
     *
     * ### Behavior
     * 1. Identifies the query type from the provided [data].
     * 2. Uses [com.apollographql.cache.normalized.removeOperation] to purge the data from the store.
     * 3. Publishes changes to trigger [watch] updates across the app.
     *
     * @param data The data object used to identify the cache entries to remove.
     */
    override suspend fun <D : Operation.Data> refresh(
        data: D?
    ) {
        when (data) {
            is GetMyPersonalProjectsQuery.Data -> {
                val query = GetMyPersonalProjectsQuery()
                apolloClient.apolloStore.removeOperation(
                    operation = query, data = data, publish = true
                )
            }

            is GetProjectDetailsQuery.Data -> {
                val query = GetProjectDetailsQuery(data.project?.id!!)
                apolloClient.apolloStore.removeOperation(operation = query, data, publish = true)
            }

            else -> Unit
        }

    }

    /**
     * **Audit Trail & Evolution**: Streams a sequentially merged log of repository commit activity.
     *
     * Utilizes specialized [fetchAndMergeCommits] logic to manually manage pagination. 
     * New pages are appended to the existing cached list rather than replacing it, 
     * ensuring smooth infinite scrolling and data persistence in the UI.
     *
     * @param id The project identifier or full path.
     * @param branch The targeted branch name.
     * @param cursor Optional pagination pointer (GitLab's `endCursor`).
     * @return A [Flow] emitting the combined, deduplicated commit history.
     * @see <a href="file:///app/src/main/graphql/com/ahtat204/project/repository/GetRepositoryCommits.graphql">GetRepositoryCommits.graphql</a>
     */
    override suspend fun getProjectCommits(
        id: String, branch: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?> {
        return apolloClient.query(
            GetRepositoryCommitsQuery(
                id, branch = branch, cursor = Optional.presentIfNotNull(cursor)
            )
        ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
            .fetchAndMergeCommits(client = apolloClient, branch, id, cursor)
    }

    /**
     * **Reference Explorer**: Lists the available development branches for repository navigation.
     *
     * Provides a paginated list of git branches. This is the backend provider for branch 
     * switchers and repository browsers, allowing users to toggle between different versions of code.
     *
     * @param project The project identifier or full path.
     * @param skip Pseudo-pagination offset for branch navigation.
     * @return A [Flow] emitting the branch registry for the repository.
     * @see <a href="file:///app/src/main/graphql/com/ahtat204/project/repository/GetRepositoryBranches.graphql">GetRepositoryBranches.graphql</a>
     */
    override suspend fun getRepositoryBranches(
        project: String, skip: Int
    ): Flow<GetRepositoryBranchesQuery.Data> {
        return apolloClient.query(GetRepositoryBranchesQuery(project, skip))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }

    /**
     * **FileSystem Navigator**: Streams the structural tree of files and directories.
     *
     * Provides the backbone for the repository file explorer. It reactively streams 
     * directory content for any given path, enabling seamless navigation through 
     * the project's source hierarchy.
     *
     * @param id The project identifier or full path.
     * @param branch Optional branch name (defaults to repository root).
     * @param path Optional relative path to a sub-directory.
     * @return A [Flow] emitting the current directory's children (blobs/trees).
     * @see <a href="file:///app/src/main/graphql/com/ahtat204/project/repository/GetProjectRepository.graphql">GetProjectRepository.graphql</a>
     */
    override suspend fun getProjectRepository(
        id: String, branch: String?, path: String?
    ): Flow<GetProjectRepositoryQuery.Data?> {
        return apolloClient.query(
            GetProjectRepositoryQuery(
                id,
                branch = Optional.presentIfNotNull(branch),
                path = Optional.presentIfNotNull(path)
            )
        ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }
}
