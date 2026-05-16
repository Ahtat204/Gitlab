package com.asue24.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.exception.CacheMissException
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetProjectDetailsQuery
import com.asue24.gitlab.data.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val _projects = MutableStateFlow<GetMyProjectsQuery.CurrentUser?>(null)

    /** Public immutable flow of contributed projects. */
    val projects: StateFlow<GetMyProjectsQuery.CurrentUser?> = _projects.asStateFlow()

    /**
     * Loads all projects contributed by the authenticated user.
     *
     * - First attempts with [FetchPolicy.CacheFirst].
     * - On exception, retries with [FetchPolicy.NetworkFirst].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadAllProjects() {
        viewModelScope.launch {
            try {
                projectRepository.getAllProjects(FetchPolicy.CacheFirst).collect { data ->
                    _projects.value = data.currentUser
                }
            } catch (ex: Exception) {
                if (ex is CacheMissException) {
                    projectRepository.getAllProjects(FetchPolicy.NetworkFirst).collect { data ->
                        _projects.value = data.currentUser
                    }
                }
                if (ex is CancellationException) throw ex
            }
        }
    }

    /**
     * Loads a specific project’s details and statistics,such as commit counts and last commit , open MRs and issues count...
     *
     * @param id The unique project identifier.
     */
    fun loadProject(id: String) {
        viewModelScope.launch {
            try {
                projectRepository.getProjectById(id, FetchPolicy.CacheFirst).collect {
                    currentProject.value = it?.project
                }
            } catch (ex: Exception) {
                if (ex is CacheMissException) {
                    projectRepository.getProjectById(id, FetchPolicy.NetworkFirst).collect {
                        currentProject.value = it?.project
                    }
                }
                if (ex is CancellationException) {
                    throw ex
                }
            }
        }
    }
}
