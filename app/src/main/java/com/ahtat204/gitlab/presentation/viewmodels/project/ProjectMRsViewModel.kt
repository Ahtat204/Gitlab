package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Type alias for the MergeRequests data structure from the GraphQL query.
 */
typealias MergeRequests = GetProjectMergeRequestsQuery.MergeRequests?

/**
 * ViewModel responsible for managing and providing Merge Request data for a specific GitLab project.
 *
 * ## Responsibilities
 * - Fetches Merge Requests from the [ProjectRepository].
 * - Exposes a reactive stream of [MergeRequests] to the UI.
 * - Handles the lifecycle of the data fetching operation using [viewModelScope].
 *
 * @param repository The repository used to fetch project-related data.
 * @author Lahcen AHTAT
 */
@HiltViewModel
class ProjectMRsViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _mrs = MutableStateFlow<MergeRequests>(null)

    /**
     * A [StateFlow] emitting the current list of Merge Requests for the project.
     * Initially null until [loadProjectMRs] is called and data is successfully fetched.
     */
    val mrs: StateFlow<MergeRequests> get() = _mrs.asStateFlow()

    /**
     * Triggers a fetch for Merge Requests associated with a specific project ID.
     *
     * The operation is performed on [Dispatchers.IO] and results are collected
     * into the [_mrs] state flow.
     *
     * @param id The unique identifier or full path of the GitLab project.
     */
    fun loadProjectMRs(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProjectMergeRequests(id).collect {
                _mrs.value = it.project?.mergeRequests
            }
        }
    }
}
