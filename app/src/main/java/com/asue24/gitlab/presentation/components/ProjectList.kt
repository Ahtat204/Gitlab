package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    LaunchedEffect (1) {
        projectViewModel.loadAllProjects()
    }
    val projectList by projectViewModel.projects.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = x, verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(projectList) { item ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { }, elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    item.project.let {name->
                        name?.name?.let {
                            Text(
                                text = it, fontSize = 18.sp, modifier = Modifier.weight(1f)
                            )
                        }
                    }?:CircularProgressIndicator()
                    Text(
                        text = "→", fontSize = 18.sp
                    )
                }
            }
        }
    }

}