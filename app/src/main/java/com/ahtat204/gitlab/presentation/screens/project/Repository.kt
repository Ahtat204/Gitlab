package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel

@Composable
fun RepositoryScreen(projectPath:String,repositoryViewModel: RepositoryViewModel= hiltViewModel()){
    LaunchedEffect(Unit) {
        repositoryViewModel.loadProjectRepository(projectPath)
    }
}