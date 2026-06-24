package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Issues = GetProjectIssuesQuery.Data?

@HiltViewModel
class IssuesViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _issues = MutableStateFlow<Issues>(null)
    val issues: StateFlow<Issues> get() = _issues.asStateFlow()
    fun loadProjectIssues(id: String) {
        viewModelScope.launch {
            projectRepository.getProjectIssues(id).collect { _issues.value = it }
        }
    }

}