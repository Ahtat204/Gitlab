package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectWorkItemsQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Project = GetProjectWorkItemsQuery.Project?

@HiltViewModel
class WorkItemsViewModel @Inject constructor(private val repository: GraphQlRepository) :
    ViewModel() {
    private val _workItems = MutableStateFlow<Project>(null)
    val workItems: StateFlow<Project> = _workItems.asStateFlow()

    fun loadProjectWorkItems(project: String) {
        val value = _workItems.value
        if (value == null) {
            viewModelScope.launch {
                repository.getProjectWorkItems(project).collect { _workItems.value = it.project }
            }

        } else {
            val page = value.workItems?.pageInfo
            val endCursor = page?.endCursor
            if (page?.hasNextPage == true && endCursor != null) {
                viewModelScope.launch {
                    repository.getProjectWorkItems(project, endCursor)
                        .collect { _workItems.value = it.project }
                }
            }
        }
    }
}