package com.ahtat204.gitlab.presentation.screens

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ahtat204.gitlab.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectCommits(
    navController: NavController,
    x: PaddingValues,
    id: String,
    projectViewModel: ProjectViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val scrolstate: ScrollState=rememberScrollState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3
        }
    }
    val commits by projectViewModel.commits.collectAsStateWithLifecycle()


    if(id!=""){
        LaunchedEffect(shouldLoadMore.value) {
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
                }
            }
        }
    }
}