package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.project.repository.CommitViewModel

@Composable
fun CommitDetails(project:String,
                  commitSha:String,
    navController: NavController,
    x: PaddingValues,
    commitViewModel: CommitViewModel = hiltViewModel()
) {

}