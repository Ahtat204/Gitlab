package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.ProfileViewModel

@Composable
fun Profile(
    navController: NavHostController,
    x: PaddingValues,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }
    val profile by profileViewModel.currentUser.collectAsState()
    profile?.let {
        Text(
            text = it.name, fontFamily = titleFont, fontSize = 20.sp, modifier = Modifier
        )
    }

}