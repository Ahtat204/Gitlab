package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.viewmodels.project.ProjectMilestonesViewModel

@Composable
fun Milestones(
    project: String,
    navHostController: NavHostController,
    x: PaddingValues,
    viewModel: ProjectMilestonesViewModel = hiltViewModel()
) {

}