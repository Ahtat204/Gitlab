package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.viewmodels.ExploreViewModel

@Composable
fun Explore(
    navHostController: NavHostController,
    x: PaddingValues,
    viewModel: ExploreViewModel = hiltViewModel()
) {
}