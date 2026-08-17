package com.ahtat204.gitlab.presentation.viewmodels.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectMilestonesQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Milestones = GetProjectMilestonesQuery.Milestones?

@HiltViewModel
class MilestonesViewModel @Inject constructor(private val repository: GraphQlRepository) :
    ViewModel() {
    private val _milestones = MutableStateFlow<Milestones>(null)
    val milestones: StateFlow<Milestones> = _milestones.asStateFlow()
    fun loadProjectMilestones(project: String) {
        val value = _milestones.value
        if (value == null) {
            viewModelScope.launch {
                repository.getProjectMilestones(project)
                    .collect { _milestones.value = it.project?.milestones }
            }
        } else {
            val page = value.pageInfo
            val hasNextPage = page.hasNextPage
            val cursor = page.endCursor
            if (hasNextPage && cursor != null) {
                viewModelScope.launch {
                    repository.getProjectMilestones(project, cursor)
                        .collect { _milestones.value = it.project?.milestones }
                }
            }
        }
    }
}