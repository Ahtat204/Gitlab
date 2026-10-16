package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Issues = GetProjectIssuesQuery.Project?

/**
 * ViewModel responsible for managing and providing issue data for a specific project.
 *
 * This class interacts with the [GraphQlRepository] to fetch paginated project issues
 * and exposes them via a lifecycle-aware asynchronous stream ([StateFlow]). It handles
 * both the initial data load and subsequent pagination requests.
 *
 * @property projectRepository The repository used to execute GraphQL queries for fetching project data.
 */
@HiltViewModel
class IssuesViewModel @Inject constructor(private val projectRepository: GraphQlRepository) :
    ViewModel() {
    /**
     * Internal mutable backing property for the stream of [Issues].
     */
    private val _issues = MutableStateFlow<Issues>(null)

    /**
     * A read-only [StateFlow] exposing the current project issues state to the UI layer.
     * Yields `null` if no data has been loaded or if the initial request is pending.
     */
    val issues: StateFlow<Issues> get() = _issues.asStateFlow()

    /**
     * Loads or paginates the issues associated with the specified project [id].
     *
     * - **Initial Load:** If no issues are currently loaded, this triggers a fetch for the first page.
     * - **Pagination:** If data is already present, it checks [Issues.issues] metadata (`pageInfo`).
     * If a next page exists (`hasNextPage == true`) and an [endCursor][com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery.PageInfo.endCursor]
     * is available, it requests the next batch of issues using that cursor.
     *
     * *Note: The current implementation overrides the existing `_issues.value` with the freshly fetched
     * page data rather than appending it. Consider modifying this logic if a continuous scroll history is required.*
     *
     * @param id The unique identifier of the project whose issues are to be loaded.
     */
    fun loadProjectIssues(id: String) {
        val value = _issues.value
        if (value == null) {
            viewModelScope.launch {
                projectRepository.getProjectIssues(id).collect { _issues.value = it.project }
            }
        } else {
            val page = value.issues?.pageInfo
            val hasNextPage = page?.hasNextPage
            val endCursor = page?.endCursor
            if (hasNextPage == true && endCursor != null) {
                viewModelScope.launch {
                    projectRepository.getProjectIssues(id, endCursor)
                        .collect { _issues.value = it.project }
                }
            }
        }

    }

}