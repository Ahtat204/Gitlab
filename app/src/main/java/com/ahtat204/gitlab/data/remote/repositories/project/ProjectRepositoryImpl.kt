package com.ahtat204.gitlab.data.remote.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.fetchAndMergeCommits
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.remote.repositories.mapAndHandleErrors
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
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
 * - [GetMyPersonalProjectsQuery], [GetProjectDetailsQuery],[GetProjectRepositoryQuery],[GetRepositoryCommitsQuery],[GetRepositoryBranchesQuery]: Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 * @author Lahcen AHTAT
 */
@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProjectRepository {
    /**
     * Streams all projects the authenticated user has contributed to.
     * @return A [Flow] emitting [GetMyPersonalProjectsQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetMyPersonalProjectsQuery] with the provided fetch policy.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Filters out null results with `mapNotNull`.
     * - Logs exceptions with [Log.e] while keeping the stream alive.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * ### Implementation Example
     * ```
     * override suspend fun getAllProjects(): Flow<GetMyProjectsPaginatedQuery.Data> =
     *         apolloClient.query(GetMyProjectsPaginatedQuery()).fetchPolicy(FetchPolicy.CacheFirst)
     *             .watch().mapNotNull { it.data }.catch { ex ->
     *                 if (ex is CancellationException) throw ex
     *                 else Log.d(ex.cause,ex.message)
     *             }.mapNotNull { it }
     *
     * ```
     *
     *
     * ### Usage example in ViewModel
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getAllProjects(FetchPolicy.CacheFirst)
     *         .collect { projects -> renderProjects(projects) }
     * }
     * ```
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
    @OptIn(ApolloExperimental::class)
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
     * ### Implementation Example
     * ```kotlin
     *  override suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?> {
     *         return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(FetchPolicy.CacheFirst)
     *             .watch().mapNotNull { it.data }.catch { ex ->
     *                 if (ex is CancellationException) throw ex
     *             }.mapNotNull { it }
     *     }
     * ```
     *
     *
     * ### Usage Example in ViewModel
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getProjectById("12345")
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
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
     * Retrieves the repository tree for a given project.
     *
     * @param id The unique identifier of the project.
     * @param path the path of the folder you want to open
     * @param branch the branch of the repository
     * @return A [Flow] emitting [GetProjectRepositoryQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectRepositoryQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * ### Implementation Example
     * ```kotlin
     *     override suspend fun getProjectRepository(id: String,skip:Int,branch:String?): Flow<GetProjectRepositoryQuery.Data?> {
     *       return  if(branch==null) {
     *             apolloClient.query(GetProjectRepositoryQuery(id,skip = skip))
     *                 .fetchPolicy(FetchPolicy.CacheFirst)
     *                 .watch().mapNotNull { it.data }
     *                 .catch { ex ->
     *                 if (ex is CancellationException) throw ex
     *             }.mapNotNull { it }
     *         }
     *         else{
     *           apolloClient.query(GetProjectRepositoryQuery(id,skip = skip, branch = Optional.present(branch)))
     *               .fetchPolicy(FetchPolicy.CacheFirst).watch()
     *               .mapNotNull { it.data }.catch { ex ->
     *               if (ex is CancellationException) throw ex
     *           }.mapNotNull { it }
     *         }
     *     }
     * ```
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getProjectRepository("12345")
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
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
     * Retrieves the repository tree for a given project.
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
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getProjectCommits("12345")
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
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
     * Streams all Personal projects the authenticated user has contributed to.
     * @return A [Flow] emitting [GetMyContributedProjectsQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetMyContributedProjectsQuery] with the provided fetch policy.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
     * - Filters out null results with `mapNotNull`.
     * - Logs exceptions with [Log.e] while keeping the stream alive.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     * ### Implementation Example
     * ```
     * override suspend fun getAllMyContributedProjects(): Flow<GetMyContributedProjectsQuery.Data> =
     *         apolloClient.query(GetMyContributedProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst)
     *             .watch().mapNotNull { it.data }.catch { ex ->
     *                 if (ex is CancellationException) throw ex
     *                 else Log.d(ex.cause,ex.message)
     *             }.mapNotNull { it }
     *
     * ```
     * ### Usage example in ViewModel
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getAllMyContributedProjects()
     *         .collect { projects -> renderProjects(projects) }
     * }
     * ```
     * Query Example:
     * ```
     *    currentUser {
     *             contributedProjects(first: 20,includePersonal: true,after: $cursor){
     *                 pageInfo {
     *                     startCursor
     *                     hasNextPage
     *                     hasPreviousPage
     *                 }
     *                 nodes {
     *                     id
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
     *
     *     }
     * ```
     */
    override suspend fun getAllMyContributedProjects(): Flow<GetMyContributedProjectsQuery.Data> {
        return apolloClient.query(GetMyContributedProjectsQuery())
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
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
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getRepositoryBranches("12345",20)
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
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

}

