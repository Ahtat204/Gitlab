package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.presentation.components.withCacheFallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for exposing GitLab project data to the UI layer.
 *
 * ## Overview
 * - Integrates with [com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository] to fetch project lists and repository trees.
 * - Uses Kotlin [kotlinx.coroutines.flow.StateFlow] to provide reactive, lifecycle‑aware state to the UI.
 * - Scoped with [dagger.hilt.android.lifecycle.HiltViewModel] for dependency injection and lifecycle management.
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

    /**
     * Loads all projects contributed by the authenticated user.
     *
     * - First attempts with [com.apollographql.apollo.cache.normalized.FetchPolicy.CacheFirst].
     * - On exception, retries with [com.apollographql.apollo.cache.normalized.FetchPolicy.NetworkFirst].
     */
    fun loadAllProjects() = viewModelScope.launch(Dispatchers.IO) {
        projectRepository.getAllProjects().withCacheFallback { projectRepository.getAllProjects() }
            .collect { _projects.value = it.currentUser }
    }

    /**
     * Loads a specific project’s details and statistics,such as commit counts and last commit , open MRs and issues count...
     *
     * @param id The unique project identifier.
     */
    fun loadProject(id: String) = viewModelScope.launch(Dispatchers.IO) {
        projectRepository.getProjectById(id).withCacheFallback {
            projectRepository.getProjectById(
                id
            )
        }.collect { currentProject.value = it?.project }
    }

}