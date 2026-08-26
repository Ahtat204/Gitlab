package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for exposing GitLab project data to the UI layer.
 *
 * ## Overview
 * - Integrates with [com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository] to fetch project lists and repository trees.
 * - Uses Kotlin [kotlinx.coroutines.flow.StateFlow] to provide reactive, lifecycle‑aware state to the UI.
 * - Scoped with [dagger.hilt.android.lifecycle.HiltViewModel] for dependency injection and lifecycle management.
 *
 * ## State
 * - [projects]: Holds the authenticated user’s contributed projects.
 * - [currentProject]: Holds the currently selected project’s details.
 *
 * ## Behavior
 * - **loadAllProjects()**: Fetches all projects using Apollo caching. Falls back
 *   to `NetworkFirst` policy if cache retrieval fails.
 * - **loadProject(id)**: Retrieves a specific project’s repository details by ID.
 * - **refreshProjects()**: Manually invalidates the project cache and triggers a re-fetch.
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
 * @author Lahcen AHTAT
 */
@HiltViewModel
class ProjectViewModel @Inject constructor(private val graphQlRepository: GraphQlRepository) :
    ViewModel() {
    /** Currently selected project’s overview/details */
    val currentProject = MutableStateFlow<GetProjectDetailsQuery.Project?>(null)

    /** Backing state for contributed projects. */
    private val _projects = MutableStateFlow<GetMyPersonalProjectsQuery.CurrentUser?>(null)

    /** Public immutable flow of contributed projects. */
    val projects: StateFlow<GetMyPersonalProjectsQuery.CurrentUser?> = _projects.asStateFlow()

    /**
     * Loads all projects contributed by the authenticated user.
     *
     * - First attempts with [com.apollographql.apollo.cache.normalized.FetchPolicy.CacheFirst].
     * - On exception, retries with [com.apollographql.apollo.cache.normalized.FetchPolicy.NetworkFirst].
     */
    fun loadAllProjects() = viewModelScope.launch {
        graphQlRepository.getAllProjects().collect { _projects.value = it.currentUser }
    }

    /**
     * Loads a specific project’s details and statistics,such as commit counts and last commit , open MRs and issues count...
     *
     * @param id The unique project identifier.
     */
    fun loadProject(id: String) = viewModelScope.launch {
        graphQlRepository.getProjectById(id).collect { currentProject.value = it?.project }
    }

    /**
     * Performs a manual refresh of the contributed projects list.
     *
     * This logic:
     * 1. Invalides the current projects in the [projectRepository]'s local cache.
     * 2. Clears the local [_projects] state to ensure UI reflects a "loading" or "empty" state.
     * 3. Re-triggers [loadAllProjects] to fetch a fresh set of data from the network.
     */
    fun refreshProjects() {
        val scope = viewModelScope
        scope.launch {
            graphQlRepository.refresh(GetMyPersonalProjectsQuery.Data(_projects.value))
            _projects.value = null
            loadAllProjects()
        }

    }

    fun refetchProject(id: String) {
        val scope = viewModelScope
        val value = currentProject.value
        scope.launch {
            graphQlRepository.refresh(GetProjectDetailsQuery.Data(value))
            currentProject.value = null
            loadProject(id)
        }
    }

}