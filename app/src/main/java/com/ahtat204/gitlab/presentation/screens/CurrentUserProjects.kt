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
fun ContributedProjects(
    navController: NavController,
    x: PaddingValues,
    projectsViewModel: ProjectsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        projectsViewModel.loadContributedProjects()
    }
    val projects by projectsViewModel.projects.collectAsStateWithLifecycle()
}