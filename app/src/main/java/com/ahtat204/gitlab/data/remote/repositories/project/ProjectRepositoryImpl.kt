package com.ahtat204.gitlab.data.remote.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.fetchAndMergeCommits
import com.ahtat204.gitlab.data.fetchAndMergePipelines
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetPipelineJobQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
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
 * - Access detailed project metadata including star counts, fork counts, and descriptions.
 * - Retrieve and monitor repository tree structures (files and directories).
 * - Fetch paginated commit histories and branch lists for a project repository.
 * - Stream project merge requests with support for pagination.
 * - Provide detailed CI/CD pipeline and job information, with manual cache merging for pipelines.
 * - Handle errors gracefully with unified logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * ### Auto‑generated query classes:
 * - [GetMyPersonalProjectsQuery],
 * - [GetProjectDetailsQuery]
 * - [GetProjectRepositoryQuery]
 * - [GetRepositoryCommitsQuery]
 * - [GetRepositoryBranchesQuery]
 * - [GetProjectPipelinesQuery]
 * - [GetPipelineJobQuery]
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 * ### Usage example in ViewModel
 * <b>this applies to all methods</b>
 * ```kotlin
 * viewModelScope.launch {
 *     projectRepository.getAllProjects(FetchPolicy.CacheFirst)
 *         .collect { projects -> renderProjects(projects) }
 * }
 * ```
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
     * Retrieves detailed information for a specific CI job.
     *
     * @param project The unique identifier or full path of the GitLab project.
     * @param job The unique identifier (GID) of the target job.
     * @return A [Flow] emitting [GetPipelineJobQuery.Data] objects.
     *
     * ### Query Example:
     * ``` Graphql
    project(fullPath: $project){
    id
    job(id: $id){
    id
    duration
    createdAt
    name
    }
    }
     * ```
     */
    override suspend fun getPipelineJob(
        project: String, job: String
    ): Flow<GetPipelineJobQuery.Data> {
        return apolloClient.query(GetPipelineJobQuery(project = project, id = job))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }

    /**
     * Retrieves detailed information for a specific CI/CD pipeline.
     *
     * @param project The unique identifier or full path of the GitLab project.
     * @param pipeline The unique identifier (GID) of the target pipeline.
     * @param cursor Pagination pointer for the associated jobs list.
     * @return A [Flow] emitting [GetProjectPipelineQuery.Data] objects.
     * ``` GraphQL
    project(fullPath:$project ){
    pipeline(id: $pipline){
    id
    name
    type
    computeMinutes
    jobs(first: 20,after: $cursor){
    nodes {
    id
    createdAt
    status
    duration
    }
    pageInfo {
    endCursor
    startCursor
    hasNextPage
    }
    }
    }
    }
     * ```
     */
    override suspend fun getProjectPipeline(
        project: String, pipeline: String, cursor: String?
    ): Flow<GetProjectPipelineQuery.Data> {
        return apolloClient.query(
            GetProjectPipelineQuery(
                project = project, pipline = pipeline, cursor = Optional.presentIfNotNull(cursor)
            )
        ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }

    /**
     * Retrieves a paginated list of repository commits.
     *
     * @param id The unique identifier of the project.
     * @param cursor Pagination index.
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
        ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors().fetchAndMergeCommits(
            client = apolloClient, branch = branch, id = id, cursor = cursor
        )

    }

    /**
     * Retrieves a paginated list of project pipelines.
     *
     * @param project The unique identifier of the project or the project path.
     * @param cursor Pagination index.
     * @param status The [PipelineStatusEnum] filter.
     * @return A [Flow] emitting [GetProjectPipelinesQuery.Data] objects.
     *
     * ### Behavior
     * - Executes [GetProjectPipelinesQuery].
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

    /**
     * Retrieves a paginated list of repository branches.
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
     * Retrieves the repository tree structure for a given path and branch.
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

}
