package com.ahtat204.gitlab.data.remote.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Data
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.watch
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing GitLab project data via GraphQL.
 *
 * Provides reactive streams of project lists, details, and repository trees.
 * Implementations are expected to use Apollo GraphQL client with caching policies.
 *
 * ### Contracts:
 * - [getAllProjects]: retrieves and Streams all projects the authenticated user has contributed to.
 * - [getProjectById]: Retrieves and streams a project overview for a given project (full description, star count, fork count, ...).
 * - [getProjectRepository]: Retrieves and streams  the repository tree (blobs, trees,...) for a given project.
 * - [getProjectCommits]: Retrieves and streams the repository commits for a given project.
 * - [getRepositoryBranches]: Retrieves and streams first 20 branches in a repository.
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
    suspend fun getAllProjects(): Flow<GetMyPersonalProjectsQuery.Data>

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
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
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
    suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?>

    /**
     * Retrieves the repository tree for a given project.
     *
     * @param id The unique identifier of the project.
     * @param path the path of the folder you want to open
     * @param branch the branch of the repository
     * @return A [Flow] emitting [GetProjectDetailsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectRepositoryQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
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
    suspend fun getProjectRepository(id: String,branch:String?,path:String?=null): Flow<Data?>

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
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
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
    suspend fun getRepositoryBranches(
        project: String,
        skip: Int
    ): Flow<GetRepositoryBranchesQuery.Data>

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
    suspend fun getProjectCommits(
        id: String, branch: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?>
    /**
     * Retrieves the repository tree for a given project.
     *
     * @param id The unique identifier of the project.
     * @param cursor:(optional)  pagination index ,match Gitlab Graphql's startCursor
     * @return A [Flow] emitting [GetProjectMergeRequestsQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetProjectMergeRequestsQuery] with the provided project ID.
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Uses Apollo’s [com.apollographql.apollo.cache.normalized.watch] to continuously observe changes.
     * - Logs errors without terminating the stream.
     * - throws [kotlinx.coroutines.CancellationException] to avoid wasting resources
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     projectRepository.getProjectMergeRequests("12345")
     *         .collect { repoTree -> renderRepoTree(repoTree) }
     * }
     * ```
     * query example
     * ``` GraphQL
     *  project(fullPath: $project){
     *         mergeRequests(sort: CREATED_DESC,first: 20,after:$cursor ){
     *             nodes{
     *                 id
     *                 name
     *                 author {
     *                     name
     *                 }
     *                 createdAt
     *                 state
     *                 sourceBranch
     *                 targetBranch
     *             }
     *             pageInfo {
     *                 startCursor
     *                 hasNextPage
     *             }
     *         }
     *     }
     * ```
     */
    suspend fun getProjectMergeRequests(id: String,cursor:String?=null):Flow<GetProjectMergeRequestsQuery.Data>
}