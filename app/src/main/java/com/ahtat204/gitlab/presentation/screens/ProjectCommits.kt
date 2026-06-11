package com.ahtat204.gitlab.presentation.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.ahtat204.gitlab.presentation.viewmodels.ProjectViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun ProjectCommits(
    navController: NavController,
    x: PaddingValues,
    id: String,
    projectViewModel: ProjectViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val commits by projectViewModel.commits.collectAsStateWithLifecycle()


    if (id != "") {
        LaunchedEffect(Unit) {
            projectViewModel.loadProjectCommits(id)
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
            if (!nodes.isEmpty()) {
                Text(
                    text = "Your Projects",
                    fontFamily = titleFont,
                    fontSize = 20.sp,
                    modifier = Modifier
                )
                 LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= nodes.size - 3 ) {
                    coroutineScope.launch {
                        projectViewModel.loadProjectCommits(id)
                    }
                }
            }
    }
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = x,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = nodes, key = { item -> item?.id ?: Any() }) { commit ->
                        CommitCard(commit?.name, commit?.message)
                    }
                    item {
                        LaunchedEffect(Unit) {
                            projectViewModel.loadProjectCommits(id)
                        }
                    }
                }
            }
        }
    }
}