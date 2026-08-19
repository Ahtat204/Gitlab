package com.ahtat204.gitlab.presentation.screens.project

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.ahtat204.gitlab.presentation.components.BranchesList
import com.ahtat204.gitlab.presentation.components.FileBrowser
import com.ahtat204.gitlab.presentation.components.RepositoryHead
import com.ahtat204.gitlab.presentation.components.TreeItemCard
import com.ahtat204.gitlab.presentation.components.iso8601ToRelative
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel

/**
 * Displays the repository screen for a given project, including branch selection,
 * commit history navigation, and repository tree exploration.
 *
 * ## Purpose
 * - Provides a UI for browsing a project’s repository contents.
 * - Shows the repository header with branch, commit message, and timeline.
 * - Allows switching between branches via a modal bottom sheet.
 * - Renders the repository tree (folders and files) with [TreeItemCard].
 *
 * ## Parameters
 * @param projectPath The unique path of the project whose repository should be displayed.
 * @param x Padding values applied to the screen layout.
 * @param navController Navigation controller used for navigating to other screens (e.g., commit history).
 * @param repositoryViewModel ViewModel responsible for loading and exposing repository data.
 * Defaults to [hiltViewModel] injection.
 *
 * ## Behavior
 * - On first composition, triggers [RepositoryViewModel.loadProjectRepository] with [projectPath].
 * - Observes repository state via [collectAsStateWithLifecycle].
 * - Displays:
 *   - Repository header with branch selector, commit message, author, and timeline.
 *   - "History" button that navigates to the commits screen for the current branch.
 *   - Repository tree with folders and files rendered via [TreeItemCard].
 * - Branch selection:
 *   - Clicking the branch button toggles a [ModalBottomSheet].
 *   - The sheet lists available branches fetched via [RepositoryViewModel.loadRepositoryBranches].
 *   - Selecting a branch reloads the repository tree for that branch.
 *
 * ## Layout
 * - Root: [Column] with black background, applied padding, and full height.
 * - Header: [Row] with branch selector, commit info, and history button.
 * - Repository tree: [Column] with folders and files, scrollable.
 * - Branch sheet: [ModalBottomSheet] listing branches in a [LazyColumn].
 *
 * ## Example
 * ```
 * RepositoryScreen(
 *     projectPath = "my-group/my-project",
 *     x = PaddingValues(16.dp),
 *     navController = navController
 * )
 * ```
 *
 * ## Notes
 * - Requires API level [Build.VERSION_CODES.O] for date formatting.
 * - Branch names and commit history navigation are URL-encoded for safe routing.
 * - Ensure [RepositoryViewModel] is properly provided via Hilt for dependency injection.
 * - Requires API level [Build.VERSION_CODES.O] for date formatting.
 * - The timeline string combines author name and relative commit time.
 *  @see <img src="https://raw.githubusercontent.com/Ahtat204/Gitlab/refs/heads/main/repository.jpg"  width="300" height="700"/>
 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepositoryScreen(
    projectPath: String,
    x: PaddingValues,
    navController: NavController,
    repositoryViewModel: RepositoryViewModel = hiltViewModel()
) {
    val history = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val currentBranch = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        repositoryViewModel.loadProjectRepository(projectPath, currentBranch.value)
    }
    val repository by repositoryViewModel.repository.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(x)
            .fillMaxHeight()
            .clickable(onClick = { })
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        repository?.tree?.lastCommit?.message?.let { message ->
            repository?.rootRef?.let { rootRef ->
                repository?.tree?.lastCommit?.committedDate.let { date ->
                    val parsedDateTime = iso8601ToRelative(date as String)

                    if (currentBranch.value == null) currentBranch.value = rootRef
                    RepositoryHead(
                        { showSheet = !showSheet },
                        currentBranch,
                        message,
                        repository?.tree?.lastCommit?.author?.name,
                        parsedDateTime,
                        navController,
                        projectPath,
                        history)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            if(!history.value){
                FileBrowser(repositoryViewModel, currentBranch, projectPath, repository)
            }
            if(history.value==true){
             currentBranch.value?.let{
                 ProjectCommits(navController = navController, branch = it, id = projectPath)
             }
            }

        }

        if (showSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                LaunchedEffect(currentBranch.value) {
                    repositoryViewModel.loadRepositoryBranches(projectPath)
                }
                val branches by repositoryViewModel.branches.collectAsStateWithLifecycle()
                BranchesList(branches, repositoryViewModel, projectPath, currentBranch, x)
            }
        }
    }
}
