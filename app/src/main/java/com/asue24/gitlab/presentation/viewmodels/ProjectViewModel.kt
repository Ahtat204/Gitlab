package com.asue24.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asue24.gitlab.GetMyProjectsQuery.Node
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.data.repositories.project.ProjectRepository
import com.asue24.gitlab.data.repositories.project.ProjectRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {
    val currentProject = MutableStateFlow<GetRepoTreeQuery.Project?>(null)

    //TODO:this will be refactored to Dependency Injection,we're just testing now
    private val projectRepository: ProjectRepository = ProjectRepositoryImpl()
    private val _projects = MutableStateFlow<List<Node>>(emptyList())
    val projects: StateFlow<List<Node>> = _projects.asStateFlow()
    fun loadProject(id: String) {
        viewModelScope.launch {
            val project = projectRepository.getProjectById(id)
            currentProject.value = project
        }
    }
}
