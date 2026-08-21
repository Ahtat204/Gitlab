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
import com.ahtat204.gitlab.presentation.components.Pipeline
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.project.PipelinesViewModel

/**
 * Composable representing the Pipelines screen for a specific project.
 *
 * This screen displays a list of CI/CD pipelines filtered by status. It supports
 * infinite scrolling by automatically fetching more pipelines when the user
 * reaches the bottom of the list.
 *
 * @param project The unique identifier or full path of the GitLab project.
 * @param navController Controller used for navigating between screens.
 * @param x Padding values representing the inner padding provided by a Scaffold.
 * @param pipelinesViewModel The ViewModel responsible for managing pipeline data,
 *                           injected via Hilt.
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