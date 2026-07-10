package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectMembersQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Members = GetProjectMembersQuery.ProjectMembers?

@HiltViewModel
class MembersViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _members = MutableStateFlow<Members>(null)
    val members: StateFlow<Members> get() = _members.asStateFlow()


    fun loadProjectMembers(project:String){
        //this means the first element
        if(_members.value?.pageInfo?.hasPreviousPage==false){
            viewModelScope.launch{
            projectRepository.getProjectMembers(project).collect { _members.value=it.project?.projectMembers }
            }
        }
        else{
            val cursor =_members.value?.pageInfo?.startCursor
            viewModelScope.launch{
                projectRepository.getProjectMembers(project, cursor).collect { _members.value=it.project?.projectMembers }
            }
        }

    }
}