package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Pipelines = GetProjectPipelinesQuery.Pipelines?

/**
 * ViewModel responsible for managing and exposing the CI/CD pipeline state for a specific GitLab project.
 *
 * ### Responsibilities
 * - Exposes a reactive [StateFlow] of [Pipelines] for UI observation.
 * - Handles data fetching from [ProjectRepository].
 * - Implements logic to prevent redundant network calls (checking for existing pipeline data).
 *
 * ### State Management
 * - **pipelines**: A [StateFlow] representing the current list of pipelines.
 * - Emits `null` during initialization.
 * - Automatically updates when repository flows emit new data.
 *
 * @param projectRepository The data layer dependency used to fetch pipeline information.
 * @see [ProjectRepository]
 * @author Lahcen AHTAT
 */
@HiltViewModel
class PipelinesViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _pipelines = MutableStateFlow<Pipelines>(null)

    /**
     * Observable [StateFlow] exposing the pipeline data.
     * UI components should collect this to render the list.
     */
    val pipelines: StateFlow<Pipelines> get() = _pipelines.asStateFlow()

    /**
     * Loads project pipelines if they are not already cached in the UI state.
     *
     * ### Logic
     * - Checks the [pageInfo.hasPreviousPage] flag to avoid unnecessary re-fetching.
     * - Launches a coroutine in [viewModelScope] to collect the flow from the repository.
     *
     * @param project The full path or unique identifier of the GitLab project.
     */
    fun loadProjectPipelines(project: String) {
        if (_pipelines.value?.pageInfo?.hasPreviousPage == false) {
            viewModelScope.launch {
                projectRepository.getProjectPipelines(project = project)
                    .collect { _pipelines.value = it.project?.pipelines }
            }
        }
    }
}