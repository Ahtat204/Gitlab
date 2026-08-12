package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.components.MergeRequest
import com.ahtat204.gitlab.presentation.viewmodels.project.ProjectMRsViewModel

/**
 * Composable representing the Merge Requests screen for a specific project.
 *
 * This screen displays a list of merge requests associated with the given project.
 * It uses the [ProjectMRsViewModel] to fetch and manage the data.
 *
 * @param project The unique identifier or full path of the GitLab project.
 * @param navController Controller used for navigating between screens.
 * @param x Padding values representing the inner padding provided by a Scaffold.
 * @param mRsViewModel The ViewModel responsible for managing project merge requests,
 *                     injected via Hilt.
 */
@Composable
fun MergeRequests(
    project: String,
    navController: NavHostController,
    x: PaddingValues,
    mRsViewModel: ProjectMRsViewModel = hiltViewModel()
) {
    val mrs by mRsViewModel.mrs.collectAsStateWithLifecycle()
    LaunchedEffect(project) {
        mRsViewModel.loadProjectMRs(project)
    }
    val nodes = mrs?.nodes
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(x)
            .background(Color.Black)
    ) {
        if (nodes?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = x,
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(nodes, key = { item -> item?.id ?: Any() }) { item ->
                    item?.let { MergeRequest(it, navController = navController) }
                }
            }
        }
    }

}