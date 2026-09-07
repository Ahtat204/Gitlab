package com.ahtat204.gitlab.presentation.screens.project.build

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.project.build.PipelineViewModel

@Composable
fun Pipeline(
    project: String,
    pipeline: String,
    navController: NavController,
    x: PaddingValues,
    viewModel: PipelineViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.loadProjectPipeline(project = project, pipeline = pipeline)
    }

    val pipeline by viewModel.pipeline.collectAsStateWithLifecycle()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {}
}