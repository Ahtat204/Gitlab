package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.components.CommitCard
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel
/**
 * Displays a paginated list of commits for a given GitLab project.
 *
 * ## Purpose
 * - Fetches and renders project commits using [RepositoryViewModel].
 * - Supports infinite scrolling: automatically loads more commits when the user
 *   approaches the bottom of the list.
 * - Provides a simple UI with a title and a scrollable list of commit cards.
 *
 * ## Parameters
 * @param navController Navigation controller used for handling navigation actions.
 * @param x Padding values applied to the layout.
 * @param id The unique project identifier. If empty, the composable returns immediately.
 * @param projectViewModel ViewModel responsible for loading and exposing commit data.
 * Defaults to [hiltViewModel] injection.
 *
 * ## Behavior
 * - If [id] is empty, the composable exits early.
 * - Observes commits via [collectAsStateWithLifecycle].
 * - Triggers [RepositoryViewModel.loadProjectCommits] when [id] changes or when
 *   the user scrolls near the bottom of the list.
 * - Skips rendering if no commits are available.
 *
 * ## Layout
 * - Root container: [Column] with black background and applied padding.
 * - Title: "Commits" displayed at the top if commits exist.
 * - List: [LazyColumn] showing each commit via [CommitCard].
 * - Infinite scroll: Loads more commits when the last visible item index is within
 *   5 items of the total count.
 *
 * ## Example
 * ```
 * ProjectCommits(
 *     navController = navController,
 *     x = PaddingValues(16.dp),
 *     id = "project123"
 * )
 * ```
 *
 * ## Notes
 * - Keys for list items are derived from commit `id` or `sha` to ensure stable rendering.
 * - The `contentDescription` for icons inside [CommitCard] should be provided
 *   if accessibility is required.
 */
@Composable
fun ProjectCommits(
    navController: NavController,
    x: PaddingValues,
    branch:String,
    id: String,
    projectViewModel: RepositoryViewModel = hiltViewModel()
) {
    if (id == "") return
    val commits by projectViewModel.commits.collectAsStateWithLifecycle()
    LaunchedEffect(id) {
        projectViewModel.loadProjectCommits(id,branch)
    }
    if (commits?.nodes?.isEmpty() == true) return
    val listState = rememberLazyListState()
  val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Trigger load when user is 3 items away from the bottom
            totalItems > 0 && lastVisibleItem >= totalItems - 5
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            projectViewModel.loadProjectCommits(id,branch)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {
        commits?.nodes?.let { nodes ->
            if (nodes.isNotEmpty()) {
                Text(
                    text = "Commits",
                    fontFamily = titleFont,
                    fontSize = 20.sp,
                    modifier = Modifier
                )
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = x,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = nodes, key =  { item -> item?.id ?: item?.sha?: null.hashCode() }) { commit ->
                        CommitCard(commit?.sha, commit?.message)
                    }
                }
            }
        }
    }
}