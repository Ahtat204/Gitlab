package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.viewmodels.UserViewModel

@Composable
fun UserProfile(
    navController: NavHostController,
    x: PaddingValues,
    profileViewModel: UserViewModel = hiltViewModel()
) {

}