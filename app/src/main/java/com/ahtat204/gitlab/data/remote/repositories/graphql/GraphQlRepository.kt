package com.ahtat204.gitlab.data.remote.repositories.graphql

import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Data
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import kotlinx.coroutines.flow.Flow

/**
 * Unified Repository interface for all GitLab GraphQL operations.
 *
 * This repository serves as the **Single Source of Truth (SSOT)** for all data retrieved via GraphQL.
 * Unlike traditional domain-driven repositories, this unified approach is used to:
 * 1. **Minimize Memory Overhead**: Prevents the creation of multiple repository instances for different domains.
 * 2. **Centralize Data Logic**: Provides a single entry point for all queries, ensuring consistent caching policies.
 * 3. **Optimize Apollo Usage**: Facilitates cross-domain data consistency through Apollo's normalized cache.
 *
 * ### Key Responsibilities:
 * - **User Dashboard**: [getAllProjects] and [getMyProfile].
 * - **Project Intelligence**: [getProjectById] and [getUserProjectsByName].
 * - **Repository Browsing**: [getProjectRepository] and [getProjectCommits].
 * - **Git Metadata**: [getRepositoryBranches].
 *
 * @author Lahcen AHTAT
 */
interface GraphQlRepository {
    /**
     * Streams all projects that the currently authenticated user has contributed to.
     *
     * @return A reactive stream emitting the user's personal project collection metadata.
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
     */
    suspend fun getAllProjects(): Flow<GetMyPersonalProjectsQuery.Data>

    /**
     * Retrieves and monitors a comprehensive overview of a single project.
     *
     * This includes detailed statistics such as descriptions, star counts, fork counts,
     * and issue metrics.
     *
     * @param id The unique identifier or full path of the target GitLab project.
     * @return A reactive stream emitting the project overview dataset, or null if the project is unavailable.
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
     */
    suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?>

    /**
     * Streams the structural file hierarchy (directories and files) for a specific path and branch.
     *
     * @param id The unique identifier or full path of the target GitLab project.
     * @param branch The target git reference branch. Pass null to default to the repository's root reference.
     * @param path The relative sub-directory path to query inside the repository. Pass null to open the root folder.
     * @return A reactive stream emitting the repository tree layer layout, or null if invalid or inaccessible.
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
     */
    suspend fun getProjectRepository(id: String, branch: String?, path: String? = null): Flow<Data?>

    /**
     * Retrieves a paginated chunk of available reference branches within a repository.
     *
     * @param project The unique identifier or full path of the target GitLab project.
     * @param skip The element offset index utilized to advance paginated window frames.
     * @return A reactive stream emitting the current window slice of matching branch records.
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
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
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is canceled.
     */
    suspend fun getProjectCommits(
        id: String, branch: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?>

    /**
     * Streams a continuous FLow containing the CurrentUser Profile data.
     * @return A reactive stream emitting the Authenticated User's profile details
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is canceled.
     */
    fun getMyProfile(): Flow<GetMyProfileQuery.Data>
    /**
     * Streams all projects belonging to a specific user identified by their username.
     *
     * @param userName The unique username of the GitLab user.
     * @return A reactive stream emitting the user's project collection metadata, or null if not found.
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
     */
    suspend fun getUserProjectsByName(
        userName: String
    ): Flow<GetUserProjectsByNameQuery.Data?>
}