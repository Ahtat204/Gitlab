package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.FileViewModel

@Composable
fun FileContent(
    project: String,
    branch: String?,
    Path: String,
    viewModel: FileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadRepositoryFile(project, Path, branch)
    }
    val file by viewModel.file.collectAsStateWithLifecycle()

}