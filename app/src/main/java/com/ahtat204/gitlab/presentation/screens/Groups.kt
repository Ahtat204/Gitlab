package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.GroupsViewModel

@Composable
fun Groups(
    navController: NavController,
    x: PaddingValues,
    groupsViewModel: GroupsViewModel = hiltViewModel()
) {
}