package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        horizontalAlignment = Alignment.End
    ) {
        if (projectList.isEmpty()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(x).offset(160.dp, y = (190).dp)) { CircularProgressIndicator(modifier = Modifier.padding(x)) }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(x),
                contentPadding = x,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(projectList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            item.project?.let { project ->
                                Text(
                                    text = project.name,
                                    fontSize = 18.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}