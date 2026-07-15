package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.ProjectsViewModel

@Composable
fun CurrentUserProjects(
    navController: NavController,
    x: PaddingValues,
    projectsViewModel: ProjectsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        projectsViewModel.loadAllMyProjects()
    }
    val projects by projectsViewModel.projects.collectAsStateWithLifecycle()
}