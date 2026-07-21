package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.project.ProjectMRsViewModel

@Composable
fun MergeRequests(
    project: String,
    navController: NavController,
    x: PaddingValues,
    mRsViewModel: ProjectMRsViewModel = hiltViewModel()
) {
    val mergeRequests by mRsViewModel.mrs.collectAsStateWithLifecycle()
    LaunchedEffect(project) {
        mRsViewModel.loadProjectMRs(project)
    }
}