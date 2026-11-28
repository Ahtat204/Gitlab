package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahtat204.gitlab.presentation.components.FileContent
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.FileViewModel

@Composable
fun FileScreen(
    project: String,
    branch: String?,
    Path: String,
    x: PaddingValues,
    viewModel: FileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadRepositoryFile(project, Path, branch)
    }
    val file by viewModel.file.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(x)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        FileContent(file)
    }
}