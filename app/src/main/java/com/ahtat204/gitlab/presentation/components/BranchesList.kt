package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.activities.ui.theme.Orange
import com.ahtat204.gitlab.presentation.activities.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.Branches
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel
/**
 * Displays a scrollable list of repository branches.
 *
 * ## Purpose
 * - Provides a UI for selecting and switching between branches in a project repository.
 * - Each branch is rendered as a card with an icon and branch name.
 * - Selecting a branch reloads the repository data for that branch.
 *
 * ## Parameters
 * @param branches The [Branches] object containing available branch names.
 * @param repositoryViewModel The [RepositoryViewModel] used to load repository data when a branch is selected.
 * @param projectPath The unique path of the project whose branches are being displayed.
 * @param currentBranch A [MutableState] holding the currently selected branch name. Updated when a branch is clicked.
 * @param x Padding values applied to the list layout.
 *
 * ## Behavior
 * - Renders a [LazyColumn] with one card per branch.
 * - Each card:
 *   - Displays a branch icon tinted with [Orange].
 *   - Shows the branch name using [customFontFamily].
 *   - On click:
 *     - Calls [RepositoryViewModel.loadProjectRepository] with the selected branch.
 *     - Updates [currentBranch] with the new branch name.
 * - The list has a black background and applies the provided padding.
 *
 * ## Layout
 * - Root: [LazyColumn] with full size, black background, and padding.
 * - Each item: [Card] containing a [Row] with:
 *   - Branch icon (30.dp size).
 *   - Spacer for separation.
 *   - Branch name text.
 *
 * ## Example
 * ```
 * BranchesList(
 *     branches = branches,
 *     repositoryViewModel = repositoryViewModel,
 *     projectPath = "my-group/my-project",
 *     currentBranch = remember { mutableStateOf("main") },
 *     x = PaddingValues(16.dp)
 * )
 * ```
 *
 * ## Notes
 * - Ensure [branches] is non-null before rendering; otherwise, nothing is displayed.
 * - The click handler directly reloads repository data for the selected branch.
 * - Styling is consistent with other repository components (black background, custom font).
 */
@Composable
fun BranchesList(
    branches: Branches,
    repositoryViewModel: RepositoryViewModel,
    projectPath: String,
    currentBranch: MutableState<String?>,
    x: PaddingValues
) {
    branches?.branchNames?.let {
        branches
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentPadding = x,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(it) { branch ->
                Card(
                    {
                        if(branch == currentBranch.value) return@Card
                        repositoryViewModel.loadProjectRepository(
                            projectPath, branch
                        )
                        repositoryViewModel.folders.value.clear()
                        currentBranch.value = branch
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 10.dp)
                        .background(Color.Black)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .height(50.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.branch),
                            contentDescription = branch,
                            Modifier
                                .size(30.dp)
                                .padding(3.dp),
                            tint = Orange
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = branch,
                            fontFamily = customFontFamily,
                            modifier = Modifier.weight(0.9f)
                        )
                    }
                }
            }
        }
    }
}