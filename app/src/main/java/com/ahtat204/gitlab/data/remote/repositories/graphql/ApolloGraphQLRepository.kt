package com.ahtat204.gitlab.data.remote.repositories.graphql

import com.ahtat204.gitlab.data.fetchAndMergeCommits
import com.ahtat204.gitlab.data.fetchAndMergePipelines
import com.ahtat204.gitlab.data.mapAndHandleErrors
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.apollographql.apollo.ApolloClient
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
 * Implementation of [GraphQlRepository] that serves as the central hub for all GitLab GraphQL interactions.
 *
 * ## Architecture: Unified Repository (SSOT)
 * This class is designed as a single, consolidated repository for all domain data (Projects, Profiles, Commits, etc.).
 * By avoiding a split into multiple domain-specific repositories, we:
 * - **Reduce Allocation**: Single singleton instance injected across all ViewModels.
 * - **Ensure Consistency**: All queries share the same [ApolloClient] instance and its normalized cache.
 * - **Streamline DI**: Simplifies Dagger/Hilt configuration by providing a one-stop-shop for GraphQL data.
 *
 * ## Data Strategy
 * - **Reactive Streams**: Returns Kotlin [Flow] to provide real-time updates when the cache changes.
 * - **Normalized Caching**: Leverages Apollo's cache to minimize network requests and ensure data integrity.
 * - **Performance**: Annotated with [Singleton] to persist across the app's lifecycle without redundant object creation.
 *
 * @param apolloClient The primary GraphQL engine used for network transport and cache management.
 * @author Lahcen AHTAT
 */
@Singleton
class ApolloGraphQLRepository @Inject constructor(
    private val apolloClient: ApolloClient
) : GraphQlRepository {
    /**
     * Streams all projects the authenticated user has contributed to.
     * @return A [Flow] emitting [GetMyPersonalProjectsQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetMyPersonalProjectsQuery] with the provided fetch policy.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Filters out null results with `mapNotNull`.
     * - Logs exceptions with [android.util.Log.e] while keeping the stream alive.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * Query Example:
     * ```
     *     currentUser {
     *         avatarUrl
     *         projectMemberships(first: 10) {
     *             __typename
     *             nodes {
     *                 __typename
     *                 id
     *                 project {
     *
     *                     topics
     *                     lastActivityAt
     *                     __typename
     *                     languages {
     *                         color
     *                         name
     *                     }
     *                     name
     *                     fullPath
     *                     description
     *                     visibility
     *                     pipelines(first: 1){
     *                         nodes {
     *                             __typename
     *                             id
     *                             status
     *                         }
     *                     }
     *                 }
     *             }
     *             pageInfo {
     *                 __typename
     *                 hasNextPage
     *                 endCursor
     *             }
     *         }
     *     }
     * }
     * ```
     */
    override suspend fun getAllProjects(): Flow<GetMyPersonalProjectsQuery.Data> =
        apolloClient.query(GetMyPersonalProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).watch()
            .mapAndHandleErrors()

    /**
     * Retrieves a project overview  for a given project.(full description , star count, fork count )
     *
     * @param id The unique identifier of the project.
     * @return A [Flow] emitting [GetProjectDetailsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectDetailsQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * Query Example
     * ``` GraphQL
     *  project(fullPath: $projectPath) {
     *
     *         __typename
     *         pipelineCounts{
     *             pending
     *             running
     *         }
     *         namespace {
     *             path
     *         }
     *         openIssuesCount
     *         fullPath
     *         openMergeRequestsCount
     *         forksCount
     *         starCount
     *         id
     *         name
     *         description
     *         }
     */
    override suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?> {
        return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapAndHandleErrors()
    }

    /**
     * Retrieves profile data of the currentUser
     * @return A [Flow] emitting [GetMyProfileQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetMyProfileQuery].
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Logs errors without terminating the stream.
     * Query Example
     * ```
     *     currentUser {
     *         id
     *         name
     *         username
     *         publicEmail
     *         avatarUrl
     *         webUrl
     *         status {
     *             availability
     *             emoji
     *             message
     *
     *         }
     *         bio
     *         location
     *         github
     *         jobTitle
     *         projectCount
     *         linkedin
     *     }
     * ```
     */
    override fun getMyProfile(): Flow<GetMyProfileQuery.Data> {
        return apolloClient.query(GetMyProfileQuery()).fetchPolicy(FetchPolicy.CacheFirst).watch()
            .mapAndHandleErrors()
    }

    /**
     * Retrieves a paginated list of first 20 commits a given project repository .
     *
     * @param id The unique identifier of the project.
     * @param cursor:(optional)  pagination index ,match Gitlab Graphql's startCursor
     * @return A [Flow] emitting [GetRepositoryCommitsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetRepositoryCommitsQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * query example
     * ``` GraphQL
     *    project(fullPath: $projectPath){
     *         __typename
     *         repository {
     *             __typename
     *             branchNames(searchPattern: "*", offset: 0, limit: 100)
     *             commits(ref:"main",first: 20,after: $cursor) {
     *                 __typename
     *                 nodes {
     *                     __typename
     *                     id
     *                     sha
     *                     name
     *                     message
     *                     authorName
     *                     committedDate
     *                     signature {
     *                         __typename
     *                         verificationStatus
     *                     }
     *
     *                 }
     *                 pageInfo {
     *                     __typename
     *                     endCursor
     *                     hasNextPage
     *                     startCursor
     *                 }
     *             }
     *             __typename
     *
     *         }
     *
     *     }
     * ```
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
     * Retrieves the repository tree for a given project.
     *
     * @param project The unique identifier of the project.
     * @param skip: a pseudo-pagination key to determine how many branches you want to skip before fetching
     * @return A [Flow] emitting [GetRepositoryBranchesQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetRepositoryBranchesQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * query example
     * ``` GraphQL
     *    project(fullPath: $projectPath){
     *         id
     *         repository{
     *             branchNames(searchPattern: "*",limit: 20,offset:$skip)
     *         }
     *     }
     * ```
     */
    override suspend fun getRepositoryBranches(
        project: String, skip: Int
    ): Flow<GetRepositoryBranchesQuery.Data> {
        return apolloClient.query(GetRepositoryBranchesQuery(project, skip))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }

    /**
     * Retrieves the repository tree for a given project.
     *
     * @param id The unique identifier of the project.
     * @return A [Flow] emitting [GetProjectDetailsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectRepositoryQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources

     * query example
     * ``` GraphQL
     *     project(fullPath: $projectPath){
     *         id
     *         repository {
     *             branchNames(searchPattern: "*",limit: 20,offset: $skip)
     *             rootRef
     *         tree(ref: $branch){
     *             blobs{
     *                 nodes {
     *                     id
     *                     name
     *                     webUrl
     *                     path
     *                 }
     *
     *             }
     *
     *         }
     *         }
     *     }
     * ```
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

    /**
     * Streams all projects belonging to a specific user identified by their username.
     *
     * @param userName The unique username of the GitLab user.
     * @return A reactive stream emitting the user's project collection metadata, or null if not found.
     *
     * ### Behavior
     * - Executes [GetUserProjectsByNameQuery] with the provided username.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow using [watch] to observe changes.
     * - Handles errors gracefully via [mapAndHandleErrors].
     * - Throws [kotlinx.coroutines.CancellationException] if the collection coroutine scope is cancelled.
     */
    override suspend fun getUserProjectsByName(
        userName: String
    ): Flow<GetUserProjectsByNameQuery.Data?> {
        return apolloClient.query(GetUserProjectsByNameQuery(userName))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }

    /**
     * Manually invalidates and refreshes data in the normalized cache for specific queries.
     *
     * Currently supports:
     * - [GetMyPersonalProjectsQuery.Data]: Removes the cached project list.
     * - [GetProjectDetailsQuery.Data]
     * - [GetProjectRepositoryQuery.Data]
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

            is GetProjectRepositoryQuery.Data -> {
                val query = GetProjectRepositoryQuery(projectPath = data.project?.id!!)
                apolloClient.apolloStore.removeOperation(operation = query, data, publish = true)
            }

            is GetProjectPipelinesQuery.Data -> {
                val query = GetProjectPipelinesQuery(project = data.project!!.id)
                apolloClient.apolloStore.removeOperation(operation = query, data, publish = true)
            }

            else -> Unit
        }

    }

    /**
     * Retrieves first 20 pipelines (currently fetch the running pipelines , later will add more method arguments).
     *
     * @param project The unique identifier of the project or the project path.
     * @param cursor:(optional)  pagination index ,match Gitlab Graphql's startCursor
     * @return A [Flow] emitting [GetProjectPipelinesQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectPipelinesQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * query example
     * ``` GraphQL
     *     project(fullPath: $project){
     *
     *         pipelines(first: 20,status: RUNNING,after: $cursor){
     *             nodes {
     *                 status
     *                 jobs{
     *                     nodes {
     *                         id
     *                         name
     *                         duration
     *                         startedAt
     *                         status
     *
     *                     }
     *                 }
     *                 committedAt
     *                 createdAt
     *                 startedAt
     *                 duration
     *                 id
     *                 name
     *
     *             }
     *             pageInfo {
     *                 hasNextPage
     *                 startCursor
     *                 hasPreviousPage
     *             }
     *         }
     *     }
     * ```
     */
    override suspend fun getProjectPipelines(
        project: String, cursor: String?, status: PipelineStatusEnum
    ): Flow<GetProjectPipelinesQuery.Data> {
        return apolloClient.query(
            GetProjectPipelinesQuery(
                status = Optional.presentIfNotNull(status),
                project = project,
                cursor = Optional.presentIfNotNull(cursor)
            )
        ).fetchPolicy(
            FetchPolicy.CacheFirst
        ).watch().mapAndHandleErrors().fetchAndMergePipelines(
            client = apolloClient, project, cursor = cursor, statusEnum = status
        )

    }

}
