package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Type alias for the ProjectMembers data structure from the GraphQL query.
 */
typealias Members = GetProjectMembersQuery.ProjectMembers?

/**
 * ViewModel responsible for managing and exposing the list of members for a specific GitLab project.
 *
 * ## Responsibilities
 * - Fetches project members from [ProjectRepository].
 * - Manages pagination by tracking the [GetProjectMembersQuery.PageInfo].
 * - Exposes a reactive [members] stream to the UI.
 *
 * @param projectRepository The repository used to fetch project-related data.
 * @author Lahcen AHTAT
 */
@HiltViewModel
class MembersViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _members = MutableStateFlow<Members>(null)

    /**
     * A [StateFlow] emitting the current list of members for the project.
     * Initially null until [loadProjectMembers] is called and data is successfully fetched.
     */
    val members: StateFlow<Members> get() = _members.asStateFlow()

    /**
     * Triggers a fetch for members associated with a specific project ID or full path.
     *
     * Handles pagination automatically:
     * - If no data exists, fetches the first page.
     * - If data exists and [hasNextPage] is true, uses the [endCursor] to fetch the next page.
     *
     * @param project The unique identifier or full path of the GitLab project.
     */
    fun loadProjectMembers(project: String) {
        val scope = viewModelScope
        val value = _members.value
        if (value == null) {
            scope.launch {
                projectRepository.getProjectMembers(project)
                    .collect { _members.value = it.project?.projectMembers }
            }
        } else {
            val page = value.pageInfo
            val hasNextPage = page.hasNextPage
            val cursor = page.endCursor
            if (hasNextPage && cursor != null) {
                scope.launch {
                    projectRepository.getProjectMembers(project, cursor)
                        .collect { _members.value = it.project?.projectMembers }
                }
            }
        }

    }
}
