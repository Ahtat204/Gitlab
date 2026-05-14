package com.asue24.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.data.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    val currentProject = MutableStateFlow<GetRepoTreeQuery.Project?>(null)
    private val _projects = MutableStateFlow<GetMyProjectsQuery.CurrentUser?>(null)
    val projects: StateFlow<GetMyProjectsQuery.CurrentUser?> = _projects.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadAllProjects() {
        viewModelScope.launch {
            projectRepository.getAllProjects(FetchPolicy.CacheFirst).collect { data ->
                    _projects.value = data.currentUser
                }
        }
    }

    fun loadProject(id: String) {
        viewModelScope.launch {
            projectRepository.getProjectById(id).collect {
                currentProject.value = it?.project
            }
        }
    }
}