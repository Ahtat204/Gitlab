package com.ahtat204.gitlab.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahtat204.gitlab.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectDetailScreen(id: String) {
    val viewModel= viewModel<ProjectViewModel>()
    val project by viewModel.currentProject.collectAsStateWithLifecycle()
    LaunchedEffect (id) {
        viewModel.loadProject(id)
    }
    project?.let {
        Text("Project: ${it.name}",color = Color.White)
    } ?: CircularProgressIndicator()
}