package com.ahtat204.gitlab.presentation.viewmodels.project.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.ahtat204.gitlab.presentation.components.removeAfterKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Commits = GetRepositoryCommitsQuery.Commits?
typealias Repository = GetProjectRepositoryQuery.Repository?
typealias Branches = GetRepositoryBranchesQuery.Repository?
typealias Path=String?
typealias Name=String
/**
 * ViewModel responsible for exposing GitLab project repository data to the UI layer.
 *
 * ## Overview
 * - Integrates with [ProjectRepository] to fetch repository trees, commits, and branches.
 * - Uses Kotlin [StateFlow] to provide reactive, lifecycle‑aware state to composables.
 * - Annotated with [HiltViewModel] for dependency injection and lifecycle management.
 *
 * ## State
 * - [folders]: A [StateFlow] of a map representing the folder hierarchy.
 *   - Key: [Path] (e.g., "src/main").
 *   - Value: [Name] (e.g., "main").
 * - [commits]: Holds the current list of repository commits.
 * - [repository]: Holds the currently loaded repository tree.
 * - [branches]: Holds the list of available branches for the project.
 *
 * ## Behavior
 * - [loadProjectRepository]:
 *   - Fetches blobs and trees for the given project and branch.
 *   - Initializes the root folder entry with the project name.
 *   - Updates the folder hierarchy when navigating into subfolders.
 *   - Ensures only one active folder path is tracked by removing entries after the current key.
 * - [loadRepositoryBranches]:
 *   - Retrieves branch names for the given project.
 *   - Supports pagination via `skip`.
 *   - Updates [branches] state with results.
 * - [loadProjectCommits]:
 *   - Fetches commits for the given project and branch.
 *   - Supports pagination using `pageInfo.endCursor`.
 *   - Appends new commits to the existing list while avoiding duplicates.
 *
 * ## Error Handling
 * - Exceptions during data collection are caught and logged.
 * - Falls back to network fetch if cache retrieval fails.
 *
 * ## Usage
 * Inject into a composable or UI controller and collect flows:
 * ```kotlin
 * @Composable
 * fun RepositoryScreen(
 *     repositoryViewModel: RepositoryViewModel = hiltViewModel()
 * ) {
 *     LaunchedEffect(Unit) {
 *         repositoryViewModel.loadProjectRepository("my-group/my-project")
 *     }
 *     val repository by repositoryViewModel.repository.collectAsStateWithLifecycle()
 *     // Render repository tree...
 * }
 * ```
 *
 * ## Notes
 * - Ensure project IDs are properly encoded when passed to queries.
 * - Folder hierarchy management uses [removeAfterKey] to maintain navigation consistency.
 * - Pagination logic in commits ensures infinite scrolling without duplicates.
 *
 * @author Lahcen AHTAT
 */
@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    /**
     * A [StateFlow] representing the folder hierarchy of the repository.
     *
     * - Key: [Path] (e.g., "src/main").
     * - Value: [Name] (e.g., "main").
     *
     * The root directory is represented by `"."` and mapped to the project name.
     * Updated when navigating into subfolders.
     */
    val folders: StateFlow<LinkedHashMap<Path, Name>> =
        MutableStateFlow(LinkedHashMap<Path, Name>()).asStateFlow()

    /** Backing state for commits. */
    private val _commits = MutableStateFlow<Commits>(null)

    /**
     * Public immutable [StateFlow] of repository commits.
     *
     * Emits commit data retrieved via [loadProjectCommits].
     */
    val commits: StateFlow<Commits> = _commits.asStateFlow()

    /** Backing state for repository tree. */
    private val _repository = MutableStateFlow<Repository>(null)

    /**
     * Public immutable [StateFlow] of the currently loaded repository.
     *
     * Emits repository tree data retrieved via [loadProjectRepository].
     */
    val repository: StateFlow<Repository> = _repository.asStateFlow()

    /** Backing state for branches. */
    private val _branches = MutableStateFlow<Branches>(null)

    /**
     * Public immutable [StateFlow] of repository branches.
     *
     * Emits branch data retrieved via [loadRepositoryBranches].
     */
    val branches: StateFlow<Branches> = _branches.asStateFlow()

    /**
     * Loads the repository tree for the given project and branch.
     *
     * @param projectPath The unique project ID (full path).
     * @param branch The branch reference to load. Defaults to the rootRef if null.
     * @param folderName The name of the folder being navigated into.
     * @param folderPath The path of the folder being navigated into.
     *
     * ## Behavior
     * - Fetches blobs and trees for the given project and branch.
     * - Initializes the root folder entry with the project name.
     * - Updates the folder hierarchy when navigating into subfolders.
     * - Ensures only one active folder path is tracked by removing entries after the current key.
     */
    fun loadProjectRepository(
        projectPath: String,
        branch: String? = null,
        folderName: String? = null,
        folderPath: String? = null
    ) {
        viewModelScope.launch {
            projectRepository.getProjectRepository(projectPath, branch = branch, path = folderPath)
                .collect {
                    _repository.value = it?.project?.repository
                    if (folders.value.isEmpty()) {
                        it?.project?.name?.let { projectName ->
                            folders.value["."] = projectName
                            if (folders.value.size > 1 || folderPath == null) {
                                folders.value.removeAfterKey(".")
                            }
                        }
                    }
                    if (folderPath != null) {
                        folderName?.let {
                            folders.value[folderPath] = folderName
                        }
                        if (folders.value.size > 1) {
                            folders.value.removeAfterKey(folderPath)
                        }
                    }
                }
        }
    }

    /**
     * Loads the list of repository branches for the given project.
     *
     * @param id The unique project ID.
     * @param skip Optional pagination offset for branch names.
     *
     * ## Behavior
     * - If branches are already loaded and `skip` is provided, fetches additional branches.
     * - Otherwise, loads the first page of branches.
     * - Updates [branches] state with results.
     */
    fun loadRepositoryBranches(id: String, skip: Int? = null) {
        if (_branches.value != null && _branches.value?.branchNames?.isNotEmpty() == true && skip != null) {
            viewModelScope.launch {
                projectRepository.getRepositoryBranches(id, skip)
                    .collect { _branches.value = it.project?.repository }
            }
        } else {
            viewModelScope.launch {
                projectRepository.getRepositoryBranches(id, 0)
                    .collect { _branches.value = it.project?.repository }
            }
        }
    }

    /**
     * Loads commits for the given project and branch.
     *
     * @param id The unique project ID.
     * @param branch The branch reference to load commits from.
     *
     * ## Behavior
     * - If no pagination cursor exists, fetches the first page of commits.
     * - If a cursor exists, fetches the next page and appends new commits.
     * - Ensures no duplicate commits by filtering with distinct IDs.
     *
     * ## State Updates
     * - Updates [_commits] with the latest commit list.
     * - Supports infinite scrolling by appending new commits.
     */
    fun loadProjectCommits(id: String, branch: String) {
        val pager = commits.value?.pageInfo?.endCursor
        val isFirstPage=commits.value?.pageInfo?.startCursor
        if (isFirstPage==null) {
            viewModelScope.launch {
                projectRepository.getProjectCommits(id, cursor = null, branch = branch).collect {
                    _commits.value = it?.project?.repository?.commits
                }
            }
        } else {
            viewModelScope.launch {
                _commits.value?.nodes?.size?.let {
                    projectRepository.getProjectCommits(id, cursor=pager,branch= branch).collect { newCommits ->
                        val newNodes = newCommits?.project?.repository?.commits?.nodes
                        if (newNodes != null) {
                            _commits.update { currentState ->
                                currentState?.copy(
                                    nodes = currentState.nodes?.plus(newNodes)
                                        ?.distinctBy { item -> item?.id }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}