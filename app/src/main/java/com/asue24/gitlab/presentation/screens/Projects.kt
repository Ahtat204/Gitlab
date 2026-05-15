package com.asue24.gitlab.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.asue24.gitlab.presentation.components.PersonalProjects

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalProjectsScreen(navController: NavHostController, x: PaddingValues){
    PersonalProjects(x)
}