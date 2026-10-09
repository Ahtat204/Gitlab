package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.activities.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.components.MergeRequest
import com.ahtat204.gitlab.presentation.viewmodels.project.ProjectMRsViewModel

/**
 * Composable representing the Merge Requests screen for a specific project.
 *
 * This screen displays a list of merge requests associated with the given project.
 * It uses the [ProjectMRsViewModel] to fetch and manage the data.
 *
 * @param project The unique identifier or full path of the GitLab project.
 * @param navController Controller used for navigating between screens.
 * @param x Padding values representing the inner padding provided by a Scaffold.
 * @param viewModel The ViewModel responsible for managing project merge requests,
 *                     injected via Hilt.
 */
@Composable
fun MergeRequests(
    project: String,
    navController: NavHostController,
    x: PaddingValues,
    viewModel: ProjectMRsViewModel = hiltViewModel()
) {
    if (project.isEmpty()) return
    val mrs by viewModel.mrs.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    LaunchedEffect(project) {
        viewModel.loadProjectMRs(project)
    }
    val nodes = mrs?.mergeRequests?.nodes
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Trigger load when user is 3 items away from the bottom
            totalItems > 9 && lastVisibleItem >= totalItems - 9
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadProjectMRs(project)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(x)
    ) {
        if (nodes?.isNotEmpty() == true) {
            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Members",
                    fontFamily = titleFont,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(0.5f)
                        .offset(20.dp, 0.dp)
                )
                IconButton(
                    onClick = { viewModel.refreshMrs(project) },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = x,
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState
            ) {
                items(nodes, key = { item -> item?.id ?: Any() }) { item ->
                    item?.let { MergeRequest(it, navController = navController) }
                }
            }
        }
    }

}