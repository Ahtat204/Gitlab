package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.viewmodels.project.IssuesViewModel

@Composable
fun Issues(
    navHostController: NavHostController,
    x:  PaddingValues,
    project: String,
    issuesViewModel: IssuesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        issuesViewModel.loadProjectIssues(project)
    }
    val issues by issuesViewModel.issues.collectAsStateWithLifecycle()
}