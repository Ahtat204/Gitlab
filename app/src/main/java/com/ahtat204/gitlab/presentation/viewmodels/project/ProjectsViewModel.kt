package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Projects = GetAllProjectsQuery.ProjectMemberships?

/**
 * ViewModel responsible for managing the user's project list with pagination support.
 *
 * This ViewModel interacts with [GraphQlRepository] to fetch projects that the
 * authenticated user has access to. It maintains a paginated list of projects
 * and handles the logic for loading more items as the user scrolls.
 *
 * ## State
 * - [projects]: A [StateFlow] emitting the current list of project memberships and pagination info.
 *
 * ## Behavior
 * - **loadCurrentUserProjects()**: Triggers a fetch for the current user's projects.
 *   If the list is empty, it fetches the first page. Otherwise, it checks if more
 *   pages are available and fetches the next page using the `endCursor`.
 *
 * @property projectRepository The unified repository for GraphQL data operations.
 * @author Lahcen AHTAT
 */
@HiltViewModel
class ProjectsViewModel @Inject constructor(private val projectRepository: GraphQlRepository) :
    ViewModel() {
    /** Backing state for project memberships. */
    private val _projects = MutableStateFlow<Projects>(null)

    /** Public immutable stream of project memberships. */
    val projects: StateFlow<Projects> get() = _projects.asStateFlow()

    /**
     * Loads or resumes loading the current user's projects.
     *
     * - If no projects are loaded, fetches the initial page.
     * - If projects exist, attempts to fetch the next page based on `hasNextPage` and `endCursor` from the current page info.
     */
    fun loadCurrentUserProjects() {
        val scope = viewModelScope
        val value = _projects.value
        if (value == null) {
            scope.launch {
                projectRepository.getAllProjects(null)
                    .collect { _projects.value = it.currentUser?.projectMemberships }
            }
        } else {
            val page = value.pageInfo
            val cursor = page.endCursor
            val hasNextPage = page.hasNextPage
            if (hasNextPage && cursor != null) {
                scope.launch {
                    projectRepository.getAllProjects(cursor)
                        .collect { _projects.value = it.currentUser?.projectMemberships }
                }
            }
        }
    }
}