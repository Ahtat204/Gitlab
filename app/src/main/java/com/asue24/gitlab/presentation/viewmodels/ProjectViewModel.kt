package com.asue24.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.GetRepoTreeQuery
import com.asue24.gitlab.data.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(private val projectRepository: ProjectRepository) : ViewModel() {
    val currentProject = MutableStateFlow<GetRepoTreeQuery.Project?>(null)

    //TODO:this will be refactored to Dependency Injection,we're just testing now

    private val _projects = MutableStateFlow< GetMyProjectsQuery.CurrentUser?>(null)
    val projects: StateFlow< GetMyProjectsQuery.CurrentUser?> = _projects.asStateFlow()
    fun loadAllProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            val projects = projectRepository.getAllProjects().collect { user ->
                _projects.value = user.currentUser
            }
        }}


        fun loadProject(id: String) {
            viewModelScope.launch {
                projectRepository.getProjectById(id).collect {
                    currentProject.value = it?.project
                }
            }
        }
    }