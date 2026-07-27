package com.ahtat204.gitlab.presentation.viewmodels.project.ci

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Type alias for the Pipelines data structure (list) from the GraphQL query.
 */
typealias Pipelines = GetProjectPipelinesQuery.Pipelines?

/**
 * Type alias for a single Pipeline detail data structure from the GraphQL query.
 */
typealias Pipeline = GetProjectPipelineQuery.Pipeline?

/**
 * ViewModel responsible for managing and exposing the CI/CD pipeline state for a specific GitLab project.
 *
 * ### Responsibilities
 * - Exposes a reactive [pipelines] stream for a list of pipelines.
 * - Exposes a reactive [pipeline] stream for single pipeline details.
 * - Handles data fetching and pagination for both lists of pipelines and jobs within a pipeline.
 * - Manages status filtering for pipeline lists.
 *
 * @param projectRepository The data layer dependency used to fetch pipeline information.
 * @see [ProjectRepository]
 * @author Lahcen AHTAT
 */
@HiltViewModel
class PipelinesViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {
    private val _pipeline = MutableStateFlow<Pipeline>(null)

    /**
     * Observable [StateFlow] exposing detailed information for a specific pipeline.
     */
    val pipeline: StateFlow<Pipeline> get() = _pipeline.asStateFlow()

    private val _pipelines = MutableStateFlow<Pipelines>(null)

    /**
     * Observable [StateFlow] exposing the list of pipelines for a project.
     * UI components should collect this to render the list.
     */
    val pipelines: StateFlow<Pipelines> get() = _pipelines.asStateFlow()

    /**
     * Loads project pipelines with pagination support and status filtering.
     *
     * ### Logic
     * - If [_pipelines] is null, fetches the first page.
     * - Otherwise, uses the [endCursor] to fetch the next page if [hasNextPage] is true.
     *
     * @param project The full path or unique identifier of the GitLab project.
     * @param status The [PipelineStatusEnum] to filter pipelines by. Defaults to [PipelineStatusEnum.SUCCESS].
     */
    fun loadProjectPipelines(
        project: String, status: PipelineStatusEnum = PipelineStatusEnum.SUCCESS
    ) {
        val page = _pipelines.value?.pageInfo
        val cursor = page?.endCursor
        val hasNextPage = page?.hasNextPage
        if (_pipelines.value == null) { // first page
            viewModelScope.launch {
                projectRepository.getProjectPipelines(
                    project = project, cursor = null, status = status
                ).collect { _pipelines.value = it.project?.pipelines }
            }
        } else {
            if (hasNextPage == true) {
                viewModelScope.launch {
                    projectRepository.getProjectPipelines(
                        project = project, cursor = cursor, status = status
                    ).collect { _pipelines.value = it.project?.pipelines }
                }
            }
        }

    }

    /**
     * Loads detailed information for a specific pipeline, including paginated jobs.
     *
     * @param project The full path or unique identifier of the GitLab project.
     * @param pipeline The unique identifier (GID) of the target pipeline.
     */
    fun loadProjectPipeline(project: String, pipeline: String) {
        val page = _pipeline.value?.jobs?.pageInfo
        val cursor = page?.endCursor
        val hasNextPage = page?.hasNextPage
        if (_pipeline.value == null) {
            viewModelScope.launch {
                projectRepository.getProjectPipeline(project, pipeline).collect {
                    _pipeline.value = it.project?.pipeline
                }
            }
        } else {
            if (hasNextPage == true && cursor != null) {
                viewModelScope.launch {
                    projectRepository.getProjectPipeline(project, pipeline, cursor)
                        .collect { _pipeline.value = it.project?.pipeline }
                }
            }
        }
    }
}
