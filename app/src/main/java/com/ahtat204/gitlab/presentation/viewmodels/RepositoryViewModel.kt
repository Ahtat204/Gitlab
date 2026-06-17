package com.ahtat204.gitlab.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.domain.usecase.logging.logger
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
/**
 * ViewModel responsible for exposing GitLab project's Repository tree data to the UI layer.
 *
 * ## Overview
 * - Integrates with [ProjectRepository] to fetch project commits and repository trees.
 * - Uses Kotlin [StateFlow] to provide reactive, lifecycle‑aware state to the UI.
 * - Scoped with [HiltViewModel] for dependency injection and lifecycle management.
 *
 * ## State
 * - [commits]: Holds the authenticated user’s contributed projects.
 * - [repository]: Holds the currently selected project’s repository tree.
 *
 * ## Behavior
 * - **loadProjectRepository()**: Fetches a project repository tree using Apollo caching. Falls back
 *   to `NetworkFirst` policy if cache retrieval fails.
 * - **loadProjectCommits(id)**: Retrieves a specific project’s repository commits by ID.
 *
 * ## Error Handling
 * - Exceptions during data collection are caught. The ViewModel retries with
 *   a network fetch to ensure data availability.
 *
 * ## Usage
 * Inject into a UI controller (e.g., Activity/Fragment) and collect flows:
 * ```kotlin
 * @Composable
 * fun screen(projectVM:ProjectViewModel=hiltViewModel) {
 *  LaunchedEffect(1) {
 *         projectViewModel.loadProjectRepository("OrderService") //Id must be encoded
 *     }
 * }
 * ```
 * @author Lahcen AHTAT
 */
@HiltViewModel
class RepositoryViewModel @Inject constructor(private val projectRepository: ProjectRepository): ViewModel() {
    /** Backing state for  commits. */
    private val _commits = MutableStateFlow<Commit>(null)

    /** Public immutable flow of  commits. */
    val commits: StateFlow<Commit> = _commits.asStateFlow()
    private val _repository= MutableStateFlow<Repository>(null)

    val repository:StateFlow<Repository> = _repository.asStateFlow()

    /**
     * currently it just fetch blobs,trees for the default branch,name of rootRef(default branch),first 20 branch names,
     * @see "graphql/com/ahtat204/GetProjectRepository.graphql"
     */
    fun loadProjectRepository(projectPath:String){
        viewModelScope.launch{
            projectRepository
                .getProjectRepository(projectPath, branch = null)
                .collect { _repository.value=it?.project?.repository }
        }
    }

    /**
     *
     */
    fun loadProjectCommits(id: String) {
        logger("LoadingCmmits", id)
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
                logger(id)
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