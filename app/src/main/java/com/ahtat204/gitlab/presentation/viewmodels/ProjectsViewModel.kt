package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahtat204.gitlab.data.queries.GetAllProjectsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias AllProjects = GetAllProjectsQuery.ProjectMemberships?

@HiltViewModel
class ProjectsViewModel
@Inject constructor(private val projectRepository: ProjectRepository) : ViewModel() {
    private val _projects = MutableStateFlow<AllProjects>(null)
    val projects: StateFlow<AllProjects> = _projects.asStateFlow()
    fun loadAllMyProjects() {
        val page = projects.value?.pageInfo
        if (page?.hasPreviousPage == false) {
            viewModelScope.launch {
                projectRepository.getAllMyProjects(null)
                    .collect { _projects.value = it.currentUser?.projectMemberships }
            }
        } else {
            viewModelScope.launch {
                projectRepository.getAllMyProjects(page?.startCursor)
                    .collect { _projects.value = it.currentUser?.projectMemberships }
            }
        }

    }
}