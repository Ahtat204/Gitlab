package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectWorkItemsQuery
import com.ahtat204.gitlab.data.queries.GetProjectWorkItemsQuery.PageInfo
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Project = GetProjectWorkItemsQuery.Project?

/**
 * [ViewModel] responsible for managing and fetching work items belonging to a specific project.
 *
 * This ViewModel interacts with a GraphQL backend via [GraphQlRepository] to fetch data,
 * handles pagination utilizing end cursors, and exposes the state reactively using [StateFlow].
 *
 * @property repository The [GraphQlRepository] instance used to execute GraphQL network queries.
 */
@Suppress("DEPRECATION")
@HiltViewModel
class WorkItemsViewModel @Inject constructor(private val repository: GraphQlRepository) :
    ViewModel() {

    /**
     * Internal mutable backing property for the project state.
     */
    private val _workItems = MutableStateFlow<Project>(null)

    /**
     * A read-only [StateFlow] exposing the current [Project] state and its associated work items.
     * UI components should collect from this flow to stay updated.
     */
    val workItems: StateFlow<Project> = _workItems.asStateFlow()

    /**
     * Loads or paginates work items for a given [project].
     *
     * - **Initial Load:** If no project data exists yet (`_workItems.value == null`), it fetches
     *   the first page of work items.
     * - **Pagination:** If data already exists, it checks if a next page is available via
     *   [PageInfo.hasNextPage]. If true, it fetches the next batch using the [PageInfo.endCursor].
     *
     * *Note: Subsequent pagination requests currently overwrite [WorkItemsViewModel._workItems]'s value with the newly
     * fetched project object instead of appending items. Ensure your repository or UI handles list merging.*
     *
     * @param project The unique identifier or name of the project whose work items are requested.
     */
    fun loadProjectWorkItems(project: String) {
        val value = _workItems.value
        if (value == null) {
            viewModelScope.launch {
                repository.getProjectWorkItems(project).collect { _workItems.value = it.project }
            }
        } else {
            val page = value.workItems?.pageInfo
            val endCursor = page?.endCursor
            if (page?.hasNextPage == true && endCursor != null) {
                viewModelScope.launch {
                    repository.getProjectWorkItems(project, endCursor)
                        .collect { _workItems.value = it.project }
                }
            }
        }
    }
}