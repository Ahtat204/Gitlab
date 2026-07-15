package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetCurrentUserWorkItemsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
typealias WorkItems= GetCurrentUserWorkItemsQuery.WorkItems?
@HiltViewModel
class WorkItemsViewModel @Inject constructor(private val projectRepository: ProjectRepository):
    ViewModel() {
        private val _workItems= MutableStateFlow<WorkItems>(null)
        val workItems: StateFlow<WorkItems> = _workItems.asStateFlow()

    fun loadCurrentUserWorkItems(){
        val page=_workItems.value?.pageInfo
        if(page?.hasPreviousPage==false){
            viewModelScope.launch {
                projectRepository.getCurrentUserWorkItems().collect { _workItems.value=it.currentUser?.workItems }
            }
        }
        else viewModelScope.launch{
            projectRepository.getCurrentUserWorkItems(page?.startCursor).collect { _workItems.value=it.currentUser?.workItems }
        }
    }
    }