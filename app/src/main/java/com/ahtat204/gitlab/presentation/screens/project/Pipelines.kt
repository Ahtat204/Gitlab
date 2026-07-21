package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.viewmodels.project.PipelinesViewModel

@Composable
fun Pipelines(project:String,navController: NavController,x: PaddingValues,pipelinesViewModel: PipelinesViewModel= hiltViewModel()){}