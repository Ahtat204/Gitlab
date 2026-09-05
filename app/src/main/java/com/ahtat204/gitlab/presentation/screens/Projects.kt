package com.ahtat204.gitlab.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.activities.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.components.CoilCache.loader
import com.ahtat204.gitlab.presentation.components.ProjectItem
import com.ahtat204.gitlab.presentation.viewmodels.project.ProjectsViewModel

/**
 * Composable screen that displays a list of projects for the current user.
 *
 * This screen uses [ProjectsViewModel] to fetch and display projects in a [LazyColumn].
 * It includes a loading indicator while the initial data is being fetched and
 * renders each project using the [ProjectItem] component.
 *
 * @param navController The controller used for navigating between screens.
 * @param x Padding values typically provided by a [androidx.compose.material3.Scaffold].
 * @param viewModel The ViewModel providing project data and business logic.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Projects(
    navController: NavHostController,
    x: PaddingValues,
    viewModel: ProjectsViewModel = hiltViewModel()
) {
    LaunchedEffect(1) {
        viewModel.loadCurrentUserProjects()
    }
    val currUser by viewModel.projects.collectAsState()
    currUser?.nodes?.let { nodes ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(x)
                .background(Color.Black)
        ) {
            if (currUser?.nodes?.isEmpty() == true) {
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
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(nodes, key = { item -> item?.id ?: Any() }) { item ->
                        item?.project?.let { ProjectItem(it, loader, navController) }
                    }
                }
            }
        }
    }
}