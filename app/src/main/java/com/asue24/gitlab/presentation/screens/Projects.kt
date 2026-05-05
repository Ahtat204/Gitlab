package com.asue24.gitlab.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.asue24.gitlab.presentation.components.ProjectList

@Composable
fun ProjectsScreen(navController: NavHostController,x: PaddingValues){
    ProjectList(x)
}