package com.ahtat204.gitlab.data.remote.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Data
import com.apollographql.apollo.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow
/**
 * Repository interface for accessing GitLab project data via GraphQL.
 *
 * Provides reactive streams of project lists, details, and repository trees.
 * Implementations are expected to use Apollo GraphQL client with caching policies.
 *  * ### Contracts:
 * - [ProjectRepository.getAllProjects]: Streams all projects the authenticated user has contributed to.
 * - [ProjectRepository.getProjectById]: Retrieves a project overview for a given project (full description, star count, fork count, ...).
 * - [ProjectRepository.getProjectRepository]: Retrieves the repository tree (blobs, trees,..) for a given project.
 * - [ProjectRepository.getProjectCommits]: Retrieves the repository commits for a given project.
 * - [ProjectRepository.getProjectIssues]: Retrieves the project issues for a given project.
 * @author Lahcen AHTAT
 */
interface ProjectRepository {
    /**
     * Streams all projects the authenticated user has contributed to.
     * @return A [Flow] emitting [GetMyProjectsPaginatedQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetMyProjectsPaginatedQuery] with the provided fetch policy.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
     * - Filters out null results with `mapNotNull`.
     * - Logs exceptions with [Log.e] while keeping the stream alive.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     *
     * ### Example
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
    suspend fun getAllProjects(): Flow<GetMyProjectsPaginatedQuery.Data>

    /**
     * Retrieves a project overview  for a given project.(full description , star count, fork count ,)
     *
     * @param id The unique identifier of the project.
     * @return A [Flow] emitting [GetProjectDetailsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectDetailsQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     *
     * ### Example
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
    suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?>
/**
 * Retrieves the repository tree for a given project.
 *
 * @param id The unique identifier of the project.
 * @return A [Flow] emitting [GetProjectRepositoryQuery.Data] objects, or null if unavailable.
 *
 * ### Behavior
 * - Executes [GetProjectRepositoryQuery] with the provided project ID.
 * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
 * - Emits results reactively via Flow.
 * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
 * - Logs errors without terminating the stream.
 * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
 *
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
    suspend fun getProjectRepository(id: String,skip:Int,branch:String?): Flow<Data?>
    /**
     * Retrieves the repository commits for a given project.
     *
     * @param id The unique identifier of the project.
     * @param cursor the pagination index to load commits after this cursor ,its match in Gitlab GraphQL is `startCursor`.
     * note :this parameter is optional
     * @return A [Flow] emitting [GetProjectCommitsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectCommitsQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
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
    suspend fun getProjectCommits(id: String, cursor: String?): Flow<GetProjectCommitsQuery.Data?>
    /**
     * Retrieves the project issues  for a given project.
     *
     * @param id The unique identifier of the project(must be encoded , mentioned in Gitlab REST documentation).
     * @return A [Flow] emitting [GetProjectCommitsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectIssuesQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getProjectIssues("12345")
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
     * query example
     * ``` GraphQL
     *       project(fullPath: $projectPath){
     *         issues(sort: CREATED_DESC){
     *             nodes {
     *                 id
     *                 name
     *                 title
     *                 state
     *                 createdAt
     *                 assignees{
     *                     nodes {
     *                         name
     *                     }
     *                 }
     *             }
     *         }
     *     }
     * ```
     */
    suspend fun getProjectIssues(id: String):Flow<GetProjectIssuesQuery.Data>

}