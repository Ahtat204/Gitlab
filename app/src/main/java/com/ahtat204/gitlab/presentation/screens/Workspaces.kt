package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.viewmodels.WorkSpacesViewModel

@Composable
fun Workspaces(
    navHostController: NavHostController,
    x: PaddingValues,
    viewModel: WorkSpacesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadWorkSpaces()
    }
    val workspaces by viewModel.workspace.collectAsStateWithLifecycle()
}