package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahtat204.gitlab.presentation.components.FileExplorer
import com.ahtat204.gitlab.presentation.viewmodels.RepositoryViewModel

@Composable
fun RepositoryScreen(projectPath:String, x: PaddingValues, repositoryViewModel: RepositoryViewModel= hiltViewModel()){
    LaunchedEffect(Unit) {
        repositoryViewModel.loadProjectRepository(projectPath)
    }
    val repository by repositoryViewModel.repository.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.padding(x)
            .fillMaxHeight()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        repository?.tree?.let {
            FileExplorer(it)
        }
    }
}