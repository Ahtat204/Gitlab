package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.presentation.components.CoilCache.loader
import com.ahtat204.gitlab.presentation.components.Pipeline
import com.ahtat204.gitlab.presentation.components.ProjectItem
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.project.PipelinesViewModel

@Composable
fun Pipelines(
    project: String,
    navController: NavController,
    x: PaddingValues,
    pipelinesViewModel: PipelinesViewModel = hiltViewModel()
) {
    val status by remember { mutableStateOf<PipelineStatusEnum?>(PipelineStatusEnum.SUCCESS) }
    LaunchedEffect(status) {
        pipelinesViewModel.loadProjectPipelines(project,status)
    }
    val pipelines by pipelinesViewModel.pipelines.collectAsStateWithLifecycle()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {
        val nodes=pipelines?.nodes
        if (nodes?.isEmpty() == false) {
            Text(
                text = "Your Projects", fontFamily = titleFont, fontSize = 20.sp, modifier = Modifier
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = x,
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = nodes, key = {item->  item?.id?:Any()}) { item ->
                    item?.let { pipeline->
                        Pipeline(pipeline.name,pipeline.status,pipeline.duration,pipeline.finishedAt) }
                }
            }
        }
    }
}