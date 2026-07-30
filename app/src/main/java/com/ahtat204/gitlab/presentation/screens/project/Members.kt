package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.project.MembersViewModel

@Composable
fun Members(
    project: String,
    navController: NavController,
    x: PaddingValues,
    viewModel: MembersViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadProjectMembers(project)
    }
    val members by viewModel.members.collectAsStateWithLifecycle()
}