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

@HiltViewModel
class ProjectMilestonesViewModel @Inject constructor(private val repository: GraphQlRepository) :
    ViewModel() {

    /** Currently selected project’s overview/details */
    private val _milestones = MutableStateFlow<GetProjectMilestonesQuery.Data?>(null)

    /** Backing state for contributed projects. */
    val milestones: StateFlow<GetProjectMilestonesQuery.Data?> = _milestones.asStateFlow()
    fun loadProjectMilestones(project: String) {
        val scope = viewModelScope
        val value = _milestones.value
        if (value == null) {
            scope.launch {
                repository.getProjectMilestones(project).collect { _milestones.value = it }
            }
        } else {
            val page = value.project?.milestones?.pageInfo
            val cursor = page?.endCursor
            if (cursor != null) {
                scope.launch {
                    repository.getProjectMilestones(project, cursor)
                        .collect { _milestones.value = it }
                }
            }
        }
    }
}