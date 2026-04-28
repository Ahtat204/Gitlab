package com.asue24.gitlab.presentation.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asue24.gitlab.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectDetailScreen(id: String,path:String, viewModel: ProjectViewModel) {
    // 1. Observe the State
    val project by viewModel.currentProject.collectAsStateWithLifecycle()
    LaunchedEffect (id) {
        viewModel.loadProject(id, path)
    }
    project?.let {
        Text("Project: ${it.name}")
    } ?: CircularProgressIndicator()
}