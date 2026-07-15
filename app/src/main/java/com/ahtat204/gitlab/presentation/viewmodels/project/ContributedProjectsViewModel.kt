package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.presentation.components.withCacheFallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributedProjectsViewModel @Inject constructor(private val projectRepository: ProjectRepository) : ViewModel() {

    /** Currently selected project’s overview/details */
    val currentProject = MutableStateFlow<GetProjectDetailsQuery.Project?>(null)

    /** Backing state for contributed projects. */
    private val _projects = MutableStateFlow<GetMyContributedProjectsQuery.CurrentUser?>(null)

    /** Public immutable flow of contributed projects. */
    val projects: StateFlow<GetMyContributedProjectsQuery.CurrentUser?> = _projects.asStateFlow()

    /**
     * Loads all projects contributed by the authenticated user.
     *
     * - First attempts with [com.apollographql.apollo.cache.normalized.FetchPolicy.CacheFirst].
     * - On exception, retries with [com.apollographql.apollo.cache.normalized.FetchPolicy.NetworkFirst].
     */
    fun loadAllProjects() = viewModelScope.launch {
        projectRepository.getAllMyContributedProjects().withCacheFallback { projectRepository.getAllMyContributedProjects() }
            .collect { _projects.value = it.currentUser }
    }

    /**
     * Loads a specific project’s details and statistics,such as commit counts and last commit , open MRs and issues count...
     *
     * @param id The unique project identifier.
     */
    fun loadProject(id: String) = viewModelScope.launch {
        projectRepository.getProjectById(id).withCacheFallback {
            projectRepository.getProjectById(
                id
            )
        }.collect { currentProject.value = it?.project }
    }
}