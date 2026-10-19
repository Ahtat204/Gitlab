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
import com.ahtat204.gitlab.presentation.activities.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.components.GitlabWorkItem
import com.ahtat204.gitlab.presentation.viewmodels.project.WorkItemsViewModel

/**
 * A Composable screen that displays a paginated list of GitLab work items for a given project.
 *
 * This component automatically triggers the initial data load when the [project] changes,
 * displays a scrollable list of items, and listens to the scroll position to automatically
 * request the next page of data as the user nears the bottom.
 *
 * ### Features:
 * - **Lifecycle Aware:** Collects state safely using [collectAsStateWithLifecycle].
 * - **Infinite Scroll Pagination:** Dynamically triggers [WorkItemsViewModel.loadProjectWorkItems]
 *   using [derivedStateOf] when the user scrolls near the last item.
 *
 * @param project The unique identifier or path of the project to load work items for.
 * @param navController The navigation controller used to handle clicks and transitions.
 * @param x The padding values propagated from a parent scaffold (e.g., top/bottom bars).
 * @param viewModel The state management instance scoped to Hilt dependency injection.
 */
@Composable
fun WorkItems(
    project: String,
    navController: NavController,
    x: PaddingValues,
    viewModel: WorkItemsViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    LaunchedEffect(project) {
        viewModel.loadProjectWorkItems(project)
    }
    val workItems by viewModel.workItems.collectAsStateWithLifecycle()
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 1 && lastVisibleItem >= totalItems - 1
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadProjectWorkItems(project)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {
        val nodes = workItems?.workItems?.nodes
        if (nodes?.isEmpty() == false) {
            Text(
                text = "Your Projects",
                fontFamily = titleFont,
                fontSize = 20.sp,
                modifier = Modifier
            )
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = nodes, key = { item -> item?.id ?: Any() }) { item ->
                    item?.let { workItem ->
                        GitlabWorkItem(
                            workItem
                        )
                    }
                }
            }
        }
    }
}