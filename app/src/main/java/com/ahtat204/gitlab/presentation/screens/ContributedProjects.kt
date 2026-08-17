package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.components.CoilCache.loader
import com.ahtat204.gitlab.presentation.components.ProjectItem
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.project.ContributedProjectsViewModel

/**
 * Composable that displays a list of projects contributed to by the current user.
 *
 * ## Overview
 * - Fetches and observes contributed project data via [ContributedProjectsViewModel].
 * - Triggers an initial project load on composition inside [LaunchedEffect].
 * - Renders a scrollable list of projects using a [LazyColumn] alongside a header.
 *
 * ## Parameters
 * @param navController The [NavHostController] used to handle route navigation when selecting a project item.
 * @param x The [PaddingValues] applied to the container for window or scaffold spacing.
 * @param viewModel The [ContributedProjectsViewModel] instance used to load and observe project data. Defaults to the Hilt-provided instance via [hiltViewModel].
 *
 * ## UI Behavior
 * - Calls [ContributedProjectsViewModel.loadAllProjects] on initial composition.
 * - Collects project state from [ContributedProjectsViewModel.projects].
 * - If projects are present:
 *   - Displays a "Your Projects" header text using the title font.
 *   - Renders a [LazyColumn] populated with [ProjectItem] instances for each valid node element.
 *
 * ## Example
 * ```kotlin
 * ContributedProjects(
 *     navController = navController,
 *     x = PaddingValues(16.dp))
 * )
 * ```
 *
 * ## Notes
 * - Uses a custom Coil image loader for loading project avatars.
 * - Relies on unique identifiers for efficient item keying within the lazy list.
 */
@Composable
fun ContributedProjects(
    navController: NavHostController,
    x: PaddingValues,
    viewModel: ContributedProjectsViewModel = hiltViewModel()
) {
    LaunchedEffect(1) {
        viewModel.loadAllProjects()
    }
    val currUser by viewModel.projects.collectAsStateWithLifecycle()
    currUser?.contributedProjects?.nodes?.let { nodes ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(x)
                .background(Color.Black)
        ) {
            if (currUser?.contributedProjects?.nodes?.isEmpty() != true) {
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
                        item?.let {
                            ProjectItem(
                                data = currUser,
                                project = it,
                                imageLoader = loader,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}