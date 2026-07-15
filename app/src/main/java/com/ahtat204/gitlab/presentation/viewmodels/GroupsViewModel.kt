package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetCurrentUserGroupsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Groups = GetCurrentUserGroupsQuery.Data?

@HiltViewModel
class GroupsViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _groups = MutableStateFlow<Groups>(null)
    val groups: StateFlow<Groups> = _groups.asStateFlow()
    fun loadUserGroups() {
        val page = _groups.value?.currentUser?.groups?.pageInfo
        if (page?.hasPreviousPage == false) {
            viewModelScope.launch {
                projectRepository.getCurrentUserGroups().collect { _groups.value = it }
            }
        } else viewModelScope.launch {
            projectRepository.getCurrentUserGroups(page?.startCursor).collect { _groups.value = it }
        }
    }
}