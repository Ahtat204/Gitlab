package com.ahtat204.gitlab.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.distinctBy
import kotlin.collections.plus
typealias Commit = GetProjectCommitsQuery.Commits?
typealias Repository= GetProjectRepositoryQuery.Repository?
@HiltViewModel
class RepositoryViewModel @Inject constructor(private val projectRepository: ProjectRepository): ViewModel() {
    /** Backing state for  commits. */
    private val _commits = MutableStateFlow<Commit>(null)

    /** Public immutable flow of  commits. */
    val commits: StateFlow<Commit> = _commits.asStateFlow()
    private val _repository= MutableStateFlow<Repository>(null)

    val repository:StateFlow<Repository> = _repository.asStateFlow()

    fun loadProjectRepository(projectPath:String){
        viewModelScope.launch{
            projectRepository
                .getProjectRepository(projectPath, branch = null, skip = 0)
                .collect { _repository.value=it?.project?.repository }
        }
    }

    fun loadProjectCommits(id: String,branch:String) {
        Log.d("LoadingCmmits", id)
        val pager = commits.value?.pageInfo?.endCursor
        if (pager == null) {
            viewModelScope.launch {
                projectRepository.getProjectCommits(id, null).collect {
                    _commits.value = it?.project?.repository?.commits
                }
            }
            return
        } else {
            viewModelScope.launch {
                Log.d("LoadingCmmits2", id)
                _commits.value?.nodes?.size?.let { it ->
                    Log.d("CursorPagerFromViewModel", pager)
                    projectRepository.getProjectCommits(id, pager).collect { newCommits ->
                        val newNodes = newCommits?.project?.repository?.commits?.nodes
                        if (newNodes != null) {
                            _commits.update { currentState ->
                                currentState?.copy(
                                    nodes = currentState.nodes?.plus(newNodes)?.distinctBy { item -> item?.id }
                                )
                            }
                        }
                    }
                }
            }
            return
        }

    }

}