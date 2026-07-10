package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.viewmodels.project.ProjectMRsViewModel

@Composable
fun ProjectMergeRequests(
    project: String,
    navController: NavHostController,
    x: PaddingValues,
    mrsViewModel: ProjectMRsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        mrsViewModel.loadProjectMRs(project)
    }
    val mergeRequest by mrsViewModel.mrs.collectAsStateWithLifecycle()
}