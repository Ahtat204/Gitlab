package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
typealias MergeRequests= GetProjectMergeRequestsQuery.MergeRequests?
@HiltViewModel
class ProjectMRsViewModel @Inject constructor(private val repository: ProjectRepository)
    : ViewModel() {
        private val _mrs=MutableStateFlow<MergeRequests>(null)
        val mrs: StateFlow<MergeRequests> = _mrs.asStateFlow()
    fun loadProjectMRs(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProjectMergeRequests(id).collect { _mrs.value=it.project?.mergeRequests }
        }
    }

    }