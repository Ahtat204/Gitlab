package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.repositories.project.ProjectRepository
import com.ahtat204.gitlab.presentation.components.withCacheFallback
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.cache.normalized.FetchPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Commit = GetProjectCommitsQuery.Commits?

/**
 * ViewModel responsible for exposing GitLab project data to the UI layer.
 *
 * ## Overview
 * - Integrates with [ProjectRepository] to fetch project lists and repository trees.
 * - Uses Kotlin [StateFlow] to provide reactive, lifecycle‑aware state to the UI.
 * - Scoped with [HiltViewModel] for dependency injection and lifecycle management.
 *
 * ## State
 * - [projects]: Holds the authenticated user’s contributed projects.
 * - [currentProject]: Holds the currently selected project’s repository tree.
 *
 * ## Behavior
 * - **loadAllProjects()**: Fetches all projects using Apollo caching. Falls back
 *   to `NetworkFirst` policy if cache retrieval fails.
 * - **loadProject(id)**: Retrieves a specific project’s repository tree by ID.
 *
 * ## Error Handling
 * - Exceptions during data collection are caught. The ViewModel retries with
 *   a network fetch to ensure data availability.
 *
 * ## Usage
 * Inject into a UI controller (e.g., Activity/Fragment) and collect flows:
 * ```kotlin
 * @Composable
 * fun screen(projectVM:ProjectViewModel=hiltViewModel) {
 *  LaunchedEffect(1) {
 *         projectViewModel.loadAllProjects()
 *     }
 * }
 * ```
 */
@HiltViewModel
class ProjectViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    /** Currently selected project’s repository tree. */
    val currentProject = MutableStateFlow<GetProjectDetailsQuery.Project?>(null)

    /** Backing state for contributed projects. */
    private val _projects = MutableStateFlow<GetMyProjectsPaginatedQuery.CurrentUser?>(null)

    /** Public immutable flow of contributed projects. */
    val projects: StateFlow<GetMyProjectsPaginatedQuery.CurrentUser?> = _projects.asStateFlow()

    /** Backing state for contributed commits. */
    private val _commits = MutableStateFlow<Commit>(null)

    /** Public immutable flow of contributed commits. */
    val commits: StateFlow<Commit> = _commits.asStateFlow()

    /**
     * Loads all projects contributed by the authenticated user.
     *
     * - First attempts with [FetchPolicy.CacheFirst].
     * - On exception, retries with [FetchPolicy.NetworkFirst].
     */
    fun loadAllProjects() = viewModelScope.launch {
        projectRepository.getAllProjects().withCacheFallback { projectRepository.getAllProjects() }
            .collect { _projects.value = it.currentUser }
    }

    /**
     * Loads a specific project’s details and statistics,such as commit counts and last commit , open MRs and issues count...
     *
     * @param id The unique project identifier.
     */
    fun loadProject(id: String) = viewModelScope.launch {
        projectRepository.getProjectById(id).withCacheFallback {
            projectRepository.getProjectById(
                id
            )
        }.collect { currentProject.value = it?.project }
    }

    fun loadProjectCommits(id: String,cursor:String?) = viewModelScope.launch {
        projectRepository.getProjectCommits(id, Optional.present(cursor))
            .withCacheFallback { projectRepository.getProjectCommits(id) }.collect {
                _commits.value = it?.project?.repository?.commits
            }
    }

}
