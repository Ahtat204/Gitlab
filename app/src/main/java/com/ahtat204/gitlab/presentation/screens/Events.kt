package com.ahtat204.gitlab.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahtat204.gitlab.presentation.viewmodels.ActivityViewModel

@Composable
fun Events(activityViewModel: ActivityViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        activityViewModel.loadEvents()
    }
}