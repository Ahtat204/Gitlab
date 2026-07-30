package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.Repository
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel
/**
 * Displays a file browser UI for navigating a GitLab project repository.
 *
 * ## Purpose
 * - Provides a breadcrumb‑style navigation bar for traversing folder hierarchy.
 * - Renders the repository tree (folders and files) with clickable items.
 * - Updates the [RepositoryViewModel] state when navigating into folders.
 *
 * ## Parameters
 * @param repositoryViewModel The [RepositoryViewModel] managing repository state and data loading.
 * @param currentBranch A [MutableState] holding the currently selected branch name.
 * @param projectPath The unique path of the project whose repository is being displayed.
 * @param repository The [Repository] object containing the current repository tree data.
 *
 * ## Behavior
 * - Breadcrumb navigation:
 *   - Displays the current folder hierarchy as clickable text items.
 *   - Clicking a breadcrumb updates [folders] in [RepositoryViewModel] and reloads the repository tree.
 *   - Ensures only one active folder path is tracked by removing entries after the clicked key.
 * - Repository tree rendering:
 *   - Displays folders (`trees.nodes`) as [TreeItemCard] components.
 *   - Displays files (`blobs.nodes`) as [TreeItemCard] components.
 *   - Clicking a folder triggers [RepositoryViewModel.loadProjectRepository] to load its contents.
 *
 * ## Layout
 * - Top section: [Row] with horizontal scroll showing breadcrumb navigation.
 * - Main section: [Column] with vertical scroll showing folders and files.
 *   - Each folder: [TreeItemCard] with folder icon and name.
 *   - Each file: [TreeItemCard] with file icon and name.
 * - Styling: Black background, thin border, rounded corners.
 *
 * ## Example
 * ```
 * FileBrowser(
 *     repositoryViewModel = repositoryViewModel,
 *     currentBranch = remember { mutableStateOf("main") },
 *     projectPath = "my-group/my-project",
 *     repository = repository
 * )
 * ```
 *
 * ## Notes
 * - Breadcrumbs are interactive and allow jumping back to parent directories.
 * - Folder hierarchy management uses [removeAfterKey] to maintain navigation consistency.
 * - Ensure [repository] is non-null before rendering; otherwise, nothing is displayed.
 */
@Composable
fun FileBrowser(
    repositoryViewModel: RepositoryViewModel,
    currentBranch: MutableState<String?>,
    projectPath: String,
    repository: Repository
) {
    Row(modifier = Modifier.padding(21.dp,0.dp).horizontalScroll(rememberScrollState())) {
        repositoryViewModel.folders.collectAsState().value.forEach { (path, name) ->
            Text(
                text = "$name \b /", modifier = Modifier
                    .offset((0).dp, ((-20).dp))
                    .clickable(
                        onClick = {
                            val value = repositoryViewModel.folders.value[path]
                            if (value == null) {
                                repositoryViewModel.folders.value[path] = name
                                if (repositoryViewModel.folders.value.size > 1) repositoryViewModel.folders.value.removeAfterKey(
                                    path
                                )
                            } else {
                                if (repositoryViewModel.folders.value.size > 1) repositoryViewModel.folders.value.removeAfterKey(
                                    path
                                )
                            }

                            repositoryViewModel.loadProjectRepository(
                                projectPath = projectPath,
                                branch = currentBranch.value,
                                folderName = name,
                                folderPath = path
                            )
                        })
            )
        }
    }
    repository?.tree?.let {
        Column(
            modifier = Modifier
                .border(
                    width = (0.1f).dp, color = Color(0xFF675353), shape = RoundedCornerShape(10.dp)
                )
                .padding(0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            it.trees.nodes?.let { folders ->
                folders.forEach { folder ->
                    TreeItemCard(
                        name = folder?.name,
                        item = folder,
                        repositoryViewModel = repositoryViewModel,
                        path = folder?.path,
                        project = projectPath,
                        branch = currentBranch.value,
                    ) {
                        folder?.let {
                            val path = repositoryViewModel.folders.value[folder.path]
                            if (path == null) {
                                repositoryViewModel.folders.value[folder.path] = folder.name
                            } else {
                                repositoryViewModel.folders.value.removeAfterKey(folder.path)
                            }
                        }
                    }
                }
            }
            it.blobs.nodes?.let { files ->
                files.forEach { file ->
                    TreeItemCard(file)
                }
            }
        }
    }

}