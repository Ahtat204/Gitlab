package com.ahtat204.gitlab.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.RepositoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchesSlider(
    branch: MutableState<String>,
    showSheet: MutableState<Boolean>,
    repositoryViewModel: RepositoryViewModel,
    projectPath: String
) {

}