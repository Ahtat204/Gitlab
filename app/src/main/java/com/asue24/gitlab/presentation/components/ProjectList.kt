package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asue24.gitlab.presentation.ui.theme.titleFont
import com.asue24.gitlab.presentation.viewmodels.ProjectViewModel

/**
 * the x:Paddingvalues parameter will be injected from the Scaffold
 */
@Composable
fun ProjectList(x: PaddingValues) {
    val projectViewModel: ProjectViewModel = viewModel()
    LaunchedEffect(1) {
        projectViewModel.loadAllProjects()
    }
    val projectList by projectViewModel.projects.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {
        if (projectList.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.offset(160.dp, y = (190).dp))
        } else {
            Text(
                text = "Your Projects",
                fontFamily = titleFont,
                fontSize = 20.sp,
                modifier = Modifier
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = x,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(projectList) { item ->
                    ProjectItem(item)
                }
            }
        }
    }
}