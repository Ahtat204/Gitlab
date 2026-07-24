package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyWorkspacesQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Type alias for the Workspace data structure from the GraphQL query.
 */
typealias Workspaces = GetMyWorkspacesQuery.Data?

/**
 * ViewModel responsible for managing and providing Remote Development Workspace data.
 *
 * ## Responsibilities
 * - Fetches the authenticated user's workspaces from [ProjectRepository].
 * - Manages pagination by tracking the [GetMyWorkspacesQuery.PageInfo] cursor.
 * - Exposes a reactive [workspace] stream to the UI.
 *
 * @param projectRepository The repository used to fetch workspace data.
 * @author Lahcen AHTAT
 */
@HiltViewModel
class WorkSpacesViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    
    private val _workspaces = MutableStateFlow<Workspaces>(null)

    /**
     * A [StateFlow] emitting the user's workspaces data.
     * Initially null until [loadWorkSpace] is called.
     */
    val workspace: StateFlow<Workspaces> = _workspaces.asStateFlow()

    /**
     * Triggers a fetch for the user's remote development workspaces.
     *
     * Handles pagination automatically:
     * - If no data exists, fetches the first page.
     * - If data exists, uses the [endCursor] from [pageInfo] to fetch the next page.
     */
    fun loadWorkSpaces() {
        val page = _workspaces.value?.currentUser?.workspaces?.pageInfo
        if (page == null) {
            viewModelScope.launch {
                projectRepository.getMyWorkspaces().collect { _workspaces.value = it }
            }
        } else {
            viewModelScope.launch {
                projectRepository.getMyWorkspaces(cursor = page.endCursor)
                    .collect { _workspaces.value = it }
            }
        }
    }
}
