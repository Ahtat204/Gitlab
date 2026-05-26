package com.ahtat204.gitlab.presentation.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahtat204.gitlab.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectDetailScreen(id: String, viewModel: ProjectViewModel = hiltViewModel()) {
    val project by viewModel.currentProject.collectAsStateWithLifecycle()
    LaunchedEffect(id) {
        viewModel.loadProject(id)
    }
    project?.let {} ?: CircularProgressIndicator()
}