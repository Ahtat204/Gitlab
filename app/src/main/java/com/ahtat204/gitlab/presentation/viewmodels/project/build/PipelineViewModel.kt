package com.ahtat204.gitlab.presentation.viewmodels.project.build

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectPipelineQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Type alias for a single Pipeline detail data structure from the GraphQL query.
 */
typealias Pipeline = GetProjectPipelineQuery.Pipeline?

/**
 * ViewModel responsible for managing and exposing the CI/CD pipeline state for a specific GitLab project.
 *
 * ### Responsibilities
 * - Exposes a reactive [pipeline] stream for single pipeline details.
 * - Handles data fetching and pagination for both lists of pipelines and jobs within a pipeline.
 * - Manages status filtering for pipeline lists.
 *
 * @param repository The data layer dependency used to fetch pipeline information.
 * @see [GraphQlRepository]
 * @author Lahcen AHTAT
 */
@HiltViewModel
class PipelineViewModel @Inject constructor(
    private val repository: GraphQlRepository
) : ViewModel() {
    private val _pipeline = MutableStateFlow<Pipeline>(null)

    /**
     * Observable [StateFlow] exposing detailed information for a specific pipeline.
     */
    val pipeline: StateFlow<Pipeline> get() = _pipeline.asStateFlow()

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
                repository.getProjectPipeline(project, pipeline).collect {
                    _pipeline.value = it.project?.pipeline
                }
            }
        } else {
            if (hasNextPage == true && cursor != null) {
                viewModelScope.launch {
                    repository.getProjectPipeline(project, pipeline, cursor)
                        .collect { _pipeline.value = it.project?.pipeline }
                }
            }
        }
    }
}
