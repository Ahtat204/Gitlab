package com.ahtat204.gitlab.data.remote.repositories.graphql

import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Data
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.apollographql.apollo.api.Operation
import kotlinx.coroutines.flow.Flow

/**
 * Unified Repository interface for all GitLab GraphQL operations.
 *
 * This repository serves as the **Single Source of Truth (SSOT)** for all data retrieved via GitLab's GraphQL API.
 * It provides a reactive API using Kotlin [Flow] and leverages Apollo's normalized cache for performance and consistency.
 *
 * ### Key Responsibilities:
 * - **User Dashboard**: Fetching personal projects [getAllPersonalProjects] and user profile [getMyProfile].
 * - **Project Intelligence**: Retrieving detailed project statistics [getProjectById] and user-specific projects [getUserProjectsByName].
 * - **Repository Browsing**: Accessing file hierarchies [getProjectRepository] and commit histories [getProjectCommits].
 * - **Git Metadata**: Listing repository branches [getRepositoryBranches].
 * - **CI/CD Visibility**: Monitoring project pipelines [getProjectPipelines].
 *
 * ### Cache Strategy:
 * Implementations should prioritize Apollo's normalized cache to ensure snappy UI transitions
 * and minimize redundant network traffic. Manual invalidation is supported via [refresh].
 *
 * @author Lahcen AHTAT
 */
interface GraphQlRepository {
    /**
     * Streams all projects that the currently authenticated user has contributed to.
     *
     * @return A reactive stream emitting the user's personal project collection metadata.
     */
    suspend fun getAllPersonalProjects(): Flow<GetMyPersonalProjectsQuery.Data>

    /**
     * Retrieves and monitors a comprehensive overview of a single project.
     *
     * This includes detailed statistics such as descriptions, star counts, fork counts,
     * and issue metrics.
     *
     * @param id The unique identifier or full path of the target GitLab project.
     * @return A reactive stream emitting the project overview dataset, or null if the project is unavailable.
     */
    suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?>

    /**
     * Streams the structural file hierarchy (directories and files) for a specific path and branch.
     *
     * @param id The unique identifier or full path of the target GitLab project.
     * @param branch The target git reference branch. Pass null to default to the repository's root reference.
     * @param path The relative sub-directory path to query inside the repository. Pass null to open the root folder.
     * @return A reactive stream emitting the repository tree layer layout, or null if invalid or inaccessible.
     */
    suspend fun getProjectRepository(id: String, branch: String?, path: String? = null): Flow<Data?>

    /**
     * Retrieves a paginated chunk of available reference branches within a repository.
     *
     * @param project The unique identifier or full path of the target GitLab project.
     * @param skip The element offset index utilized to advance paginated window frames.
     * @return A reactive stream emitting the current window slice of matching branch records.
     */
    suspend fun getRepositoryBranches(
        project: String, skip: Int
    ): Flow<GetRepositoryBranchesQuery.Data>

    /**
     * Streams a continuous, sequentially chunked record of repository commit histories.
     *
     * Implementations are expected to manage incremental page updates and item appending states.
     *
     * @param id The unique identifier or full path of the target GitLab project.
     * @param branch The targeted git branch line from which to trace commit milestones.
     * @param cursor The pagination pointer marking the anchor location for sequential page fetches. Pass null for the initial page.
     * @return A reactive stream emitting the combined commit log historical records, or null if missing.
     */
    suspend fun getProjectCommits(
        id: String, branch: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?>

    /**
     * Streams a continuous Flow containing the CurrentUser Profile data.
     *
     * @return A reactive stream emitting the Authenticated User's profile details.
     */
    fun getMyProfile(): Flow<GetMyProfileQuery.Data>

    /**
     * Streams all projects belonging to a specific user identified by their username.
     *
     * @param userName The unique username of the GitLab user.
     * @return A reactive stream emitting the user's project collection metadata, or null if not found.
     */
    suspend fun getUserProjectsByName(
        userName: String
    ): Flow<GetUserProjectsByNameQuery.Data?>

    /**
     * Manually invalidates and refreshes specific data in the normalized cache.
     *
     * Removes the existing operation data from the cache and triggers a re-fetch
     * to ensure active observers receive fresh data.
     *
     * @param D The data type of the GraphQL operation.
     * @param data The specific data object used to identify what needs removal.
     */
    suspend fun <D : Operation.Data> refresh(data: D?)

    /**
     * Streams a continuous, sequentially chunked record of project CI/CD pipelines.
     *
     * Implementations are expected to manage incremental page updates and item appending states.
     *
     * @param project The unique identifier or full path of the target GitLab project.
     * @param cursor The pagination pointer marking the anchor location for sequential page fetches. Pass null for the initial page.
     * @param status The status [PipelineStatusEnum] filter for the pipelines. Defaults to [PipelineStatusEnum.SUCCESS].
     * @return A reactive stream emitting the filtered pipeline collection metadata.
     */
    suspend fun getProjectPipelines(
        project: String,
        cursor: String? = null,
        status: PipelineStatusEnum = PipelineStatusEnum.SUCCESS
    ): Flow<GetProjectPipelinesQuery.Data>

    /**
     * Streams all projects that the currently authenticated user has access to, with pagination support.
     *
     * @param cursor The pagination pointer for sequential page fetches. Pass null for the initial page.
     * @return A reactive stream emitting the user's project memberships metadata.
     */
    suspend fun getAllProjects(cursor: String?): Flow<GetAllProjectsQuery.Data>
}