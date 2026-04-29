package com.asue24.gitlab.presentation.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asue24.gitlab.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectDetailScreen(id: String,path:String) {
    val viewModel= viewModel<ProjectViewModel>()
    // 1. Observe the State
    val project by viewModel.currentProject.collectAsStateWithLifecycle()
    LaunchedEffect (id) {
        viewModel.loadProject(id, path)
    }
    project?.let {
        Text("Project: ${it.description.toString()}",color = Color.White)
        Text("Project: ${it.topics?.get(1).toString()}",color = Color.White)
    } ?: CircularProgressIndicator()
}