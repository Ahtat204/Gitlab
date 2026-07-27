package com.ahtat204.gitlab.presentation.viewmodels.project.ci

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetPipelineJobQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Type alias for the Job data structure from the GraphQL query.
 */
typealias Job = GetPipelineJobQuery.Job?

/**
 * ViewModel responsible for managing and exposing the state of a specific CI/CD job.
 *
 * ### Responsibilities
 * - Exposes a reactive [job] stream for UI observation.
 * - Fetches detailed job information from [ProjectRepository].
 *
 * @param projectRepository The data layer dependency used to fetch job information.
 * @author Lahcen AHTAT
 */
@HiltViewModel
class JobViewModel @Inject constructor(private val projectRepository: ProjectRepository) :
    ViewModel() {
    private val _job = MutableStateFlow<Job>(null)

    /**
     * Observable [StateFlow] exposing the specific job data.
     */
    val job: StateFlow<Job> = _job.asStateFlow()

    /**
     * Triggers a fetch for detailed information about a specific CI/CD job.
     *
     * @param project The full path or unique identifier of the GitLab project.
     * @param job The unique identifier (GID) of the target job.
     */
    fun loadPipelineJob(project: String, job: String) {
        viewModelScope.launch {
            projectRepository.getPipelineJob(project = project, job).collect {
                _job.value = it.project?.job
            }
        }
    }

}
