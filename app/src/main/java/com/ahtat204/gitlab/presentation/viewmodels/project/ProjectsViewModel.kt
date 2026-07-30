package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Projects = GetAllProjectsQuery.ProjectMemberships?

@HiltViewModel
class ProjectsViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _projects = MutableStateFlow<Projects>(null)
    val projects: StateFlow<Projects> get() = _projects.asStateFlow()
    fun loadCurrentUserProjects() {
        val scope = viewModelScope
        val value = _projects.value
        if (value == null) {
            scope.launch {
                projectRepository.getAllProjects()
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