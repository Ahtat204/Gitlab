package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.data.queries.GetCommitDetailsQuery
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Data
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
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
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
     */
    suspend fun getProjectCommits(
        id: String, branch: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?>

    suspend fun getCommitDetails(sha:String,project:String):Flow<GetCommitDetailsQuery.Data>
}