package com.ahtat204.gitlab.presentation.viewmodels.project.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetCommitDetailsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Commit = GetCommitDetailsQuery.Data?

@HiltViewModel
class CommitViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _commit = MutableStateFlow<Commit>(null)
    val commit: StateFlow<Commit> = _commit.asStateFlow()
    fun loadCommitDetails(project: String, sha: String) {
        viewModelScope.launch {
            projectRepository.getCommitDetails(project = project, sha = sha)
                .collect { _commit.value = it }
        }
    }
}