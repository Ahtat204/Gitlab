package com.ahtat204.gitlab.presentation.screens.project

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.presentation.activities.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.components.Pipeline
import com.ahtat204.gitlab.presentation.viewmodels.project.PipelinesViewModel

/**
 * Composable screen responsible for rendering the CI/CD pipeline history of a GitLab project.
 *
 * ## Overview
 * This screen provides a reactive, paginated list of pipelines, allowing users to monitor
 * build statuses, runtimes, and individual job outcomes. It integrates with the
 * [PipelinesViewModel] to fetch data from GitLab's GraphQL API and utilizes Apollo's
 * normalized cache for smooth state transitions.
 *
 * ## Key Features
 * - **Infinite Scrolling**: Automatically detects when the user approaches the end of the
 *   [LazyColumn] and triggers a fetch for the next page of pipelines.
 * - **Status Filtering**: Currently defaults to successful pipelines, with underlying
 *   support for filtering by status (SUCCESS, RUNNING, FAILED, etc.).
 * - **Reactive Updates**: Utilizes [collectAsStateWithLifecycle] to ensure the UI
 *   responds immediately to cache updates or network fetches.
 * - **Optimized Rendering**: Uses stable keys in the [LazyColumn] to minimize
 *   unnecessary recompositions during pagination.
 *
 * ## UI Components
 * - **Pipeline List**: A vertical list of [Pipeline] components, each representing
 *   a single CI execution.
 * - **Loading Logic**: Implements a `shouldLoadMore` derived state to manage
 *   pagination triggers without blocking the main thread.
 *
 * @param project The unique identifier (GID) or full path of the target GitLab project.
 * @param navController The [NavController] used to handle navigation to job details or other screens.
 * @param x The [PaddingValues] provided by the parent [androidx.compose.material3.Scaffold].
 * @param pipelinesViewModel The ViewModel providing pipeline state and pagination logic.
 *
 * ## Usage
 * ```kotlin
 * Pipelines(
 *     project = "ahtat204/gitlab-client",
 *     navController = navController,
 *     x = it
 * )
 * ```
 * @see <img src="https://raw.githubusercontent.com/Ahtat204/Gitlab/refs/heads/screen/project/workitems/pipelines.jpg"  width="300" height="700"/>
 * @author Lahcen AHTAT
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pipelines(
    project: String,
    navController: NavController,
    x: PaddingValues,
    pipelinesViewModel: PipelinesViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val status by remember { mutableStateOf<PipelineStatusEnum>(PipelineStatusEnum.SUCCESS) }
    LaunchedEffect(status) {
        pipelinesViewModel.loadProjectPipelines(project, status)
    }
    val pipelines by pipelinesViewModel.pipelines.collectAsStateWithLifecycle()
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Trigger load when user is 3 items away from the bottom
            totalItems > 1 && lastVisibleItem >= totalItems - 1
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            pipelinesViewModel.loadProjectPipelines(project, status)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {
        val nodes = pipelines?.nodes
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
                    item?.let { pipeline ->
                        Pipeline(
                            pipeline
                        )
                    }
                }
            }

        }
    }
}