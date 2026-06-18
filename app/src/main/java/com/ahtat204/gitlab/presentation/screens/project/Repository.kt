package com.ahtat204.gitlab.presentation.screens.project

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahtat204.gitlab.presentation.components.FileExplorer
import com.ahtat204.gitlab.presentation.components.RepositoryHead
import com.ahtat204.gitlab.presentation.components.iso8601ToRelative
import com.ahtat204.gitlab.presentation.viewmodels.RepositoryViewModel
/**
 * Displays the repository screen for a given project.
 *
 * ## Purpose
 * - Provides a UI for exploring a project’s repository contents.
 * - Shows the repository header with branch, commit message, and timeline.
 * - Renders the repository tree via [FileExplorer].
 *
 * ## Parameters
 * @param projectPath The unique path of the project whose repository should be displayed.
 * @param x Padding values applied to the screen layout.
 * @param repositoryViewModel ViewModel responsible for loading and exposing repository data.
 * Defaults to [hiltViewModel] injection.
 *
 * ## Behavior
 * - On first composition, triggers [RepositoryViewModel.loadProjectRepository] with [projectPath].
 * - Observes repository state via [collectAsStateWithLifecycle].
 * - If repository data is available:
 *   - Displays [RepositoryHead] with commit message, author, timeline, and branch reference.
 *   - Displays [FileExplorer] with the repository tree.
 * - Dates are formatted using [iso8601ToRelative] for relative time display (e.g., "2 hours ago").
 *
 * ## Layout
 * - Root: [Column] with black background, applied padding, and full height.
 * - Top section: [RepositoryHead] showing branch and commit info.
 * - Bottom section: [FileExplorer] rendering the repository tree.
 *
 * ## Example
 * ```
 * RepositoryScreen(
 *     projectPath = "my-group/my-project",
 *     x = PaddingValues(16.dp)
 * )
 * ```
 *
 * ## Notes
 * - Requires API level [Build.VERSION_CODES.O] for date formatting.
 * - The timeline string combines author name and relative commit time.
 * - Ensure [RepositoryViewModel] is properly provided via Hilt for dependency injection.
 *  @see <img src="https://raw.githubusercontent.com/Ahtat204/Gitlab/refs/heads/screen/project/repository/repository.jpg"  width="300" height="700"/>
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepositoryScreen(
    projectPath: String,
    x: PaddingValues,
    repositoryViewModel: RepositoryViewModel = hiltViewModel()
) {
    val branch=remember { mutableStateOf<String?>(null) }
    LaunchedEffect(branch.value) {
        repositoryViewModel.loadProjectRepository(projectPath,branch.value)
    }
    val repository by repositoryViewModel.repository.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(x)
            .fillMaxHeight()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repository?.lastCommit?.message?.let { message ->
            repository?.rootRef?.let { rootRef ->
                repository?.lastCommit?.committedDate.let { date ->
                    val parsedDateTime = iso8601ToRelative(date as String)

                    if (branch.value==null) branch.value=rootRef
                    RepositoryHead(
                        commitMessage = message,
                        timeline = "${repository?.lastCommit?.author?.name} authored $parsedDateTime",
                        branch = branch
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        repository?.tree?.let {
            FileExplorer(it)
        }
    }
}