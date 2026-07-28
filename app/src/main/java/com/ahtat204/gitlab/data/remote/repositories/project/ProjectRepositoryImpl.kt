package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.data.fetchAndMergeCommits
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBlobQuery
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
     * ### Behavior
     * - Uses [FetchPolicy.CacheFirst] to show cached data immediately while fetching updates.
     * - Continuously [watch]es the cache for updates (e.g., from other queries or manual cache writes).
     * - Filters out null results and handles exceptions via `mapAndHandleErrors`.
     * ``` Graphql
     * query GetMyPersonalProjects($cursor: String){
     *     currentUser {
     *         id
     *         avatarUrl
     *         namespace {
     *             projects(first: 20,after: $cursor,sort: ACTIVITY_DESC){
     *
     *                pageInfo {
     *                    startCursor
     *                    hasNextPage
     *                    hasPreviousPage
     *                }
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
     * **Project Intelligence**: Provides deep-dive metadata and analytics for a specific GitLab project.
     *
     * Beyond basic naming, this retrieves quantitative data like star counts, fork counts, 
     * and detailed issue metrics, enabling rich project overview screens.
     *
     * @param id The unique identifier (GID) or full path of the GitLab project.
     * @return A reactive stream of project metadata, or null if inaccessible.
     * ``` Graphql
     * query GetProjectDetails($projectPath: ID!) {
     *     project(fullPath: $projectPath) {
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
     *     }
     * }
     * ```
     */
    override suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?> {
        return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapAndHandleErrors()
    }

    /**
     * **Source Code Access**: Fetches the raw content and metadata of individual repository files.
     *
     * Designed for source code inspection, this method retrieves the actual content of "blobs" 
     * (Binary Large Objects) along with metadata like web URLs and repository paths.
     *
     * @param project The project full path or GID.
     * @param branch The target git branch (e.g., "main").
     * @param path The relative path to the file within the repository.
     * @return A [Flow] emitting the file metadata and raw content.
     * ```
     * query GetRepositoryBlob($project:ID!,$branch:String,$path:[String!]!){
     *     project(fullPath:$project ){
     *         repository {
     *             blobs(first: 20,ref: $branch,paths:$path  ){
     *                 edges {
     *                     cursor
     *                     node {
     *                         rawBlob
     *                         rawTextBlob
     *                     }
     *                 }
     *             }
     *         }
     *     }
     * }
     * ```
     */
    override suspend fun getRepositoryBlob(
        project: String, branch: String, path: String
    ): Flow<GetRepositoryBlobQuery.Data> {
        return apolloClient.query(
            GetRepositoryBlobQuery(
                project, Optional.presentIfNotNull(branch), listOf(path)
            )
        ).fetchPolicy(
            FetchPolicy.CacheFirst
        ).watch().mapAndHandleErrors()
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
     * ``````
     * query GetRepositoryCommits($projectPath: ID!,$cursor:String,$branch:String!){
     *     project(fullPath: $projectPath){
     *         __typename
     *         repository {
     *             __typename
     *             branchNames(searchPattern: "*", offset: 0, limit: 100)
     *             commits(ref:$branch,first: 20,after: $cursor) {
     *                 __typename
     *                 nodes {
     *                     __typename
     *                     id
     *                     sha
     *                     name
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
     *
     * }
     * ```````
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
     *````
     * query GetRepositoryBranches($projectPath:ID!,$skip:Int!){
     *     project(fullPath: $projectPath){
     *         id
     *         repository{
     *             branchNames(searchPattern: "*",limit: 20,offset:$skip)
     *         }
     *     }
     * }
     * ````
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
     * ```
     * query GetProjectRepository($projectPath:ID!,$branch:String,$path:String){
     *     project(fullPath: $projectPath){
     *         id
     *         name
     *         repository {
     *             rootRef
     *
     *             tree(ref: $branch,path:$path){
     *                 __typename
     *                 lastCommit(ref: $branch){
     *                     message
     *                     id
     *                     committedDate
     *                     author {
     *                         name
     *                     }
     *                 }
     *                 trees{
     *                     __typename
     *
     *                     nodes {
     *                         id
     *                         name
     *                         path
     *
     *                     }
     *                     pageInfo {
     *                         startCursor
     *                     }
     *                     edges {
     *                         cursor
     *                     }
     *                 }
     *                 blobs{
     *                     nodes {
     *                         id
     *                         name
     *                         path
     *                     }
     *                     pageInfo {
     *                         startCursor
     *                     }
     *                     edges {
     *                         cursor
     *                     }
     *                 }
     *             }
     *         }
     *     }
     * }
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
