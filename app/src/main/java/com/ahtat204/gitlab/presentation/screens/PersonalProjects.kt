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
import coil.ImageLoader
import com.ahtat204.gitlab.presentation.components.CoilCache.loader
import com.ahtat204.gitlab.presentation.components.ProjectItem
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.project.PersonalProjectsViewModel
import java.time.Instant
import java.time.ZoneId

/**
 * Composable that displays the authenticated user's personal GitLab projects.
 *
 * ## Overview
 * - Fetches and observes project data via [PersonalProjectsViewModel].
 * - Shows a loading indicator until projects and avatar are available.
 * - Displays a list of projects sorted by last activity date.
 * - Delegates rendering of each project to [ProjectItem].
 *
 * ## Parameters
 * @param x The [PaddingValues] applied to the container for spacing.
 * @param personalProjectsViewModel The [PersonalProjectsViewModel] instance used to load and observe
 *                         project data. Defaults to Hilt‑provided instance via [hiltViewModel].
 *
 * ## UI Behavior
 * - Initializes a Coil [ImageLoader] with caching and crossfade enabled.
 * - Calls [PersonalProjectsViewModel.loadAllProjects] inside [LaunchedEffect] to trigger data fetch.
 * - Collects current user data from [PersonalProjectsViewModel.projects] as state.
 * - If no projects or avatar are available:
 *   - Displays a [CircularProgressIndicator].
 * - Otherwise:
 *   - Displays a "Your Projects" header.
 *   - Renders a [LazyColumn] of project items using [ProjectItem].
 *   - Projects are sorted by their `lastActivityAt` timestamp (most recent first).
 *
 * ## Example
 * ```kotlin
 * PersonalProjects(
 *     x = PaddingValues(16.dp),
 *     projectViewModel = hiltViewModel()
 * )
 * ```
 *
 * ## Notes
 * - Uses [Instant] and [ZoneId] to sort projects by activity date.
 * - Relies on [ProjectItem] composable to render individual project details.
 * - Displays up to all available projects; topics and languages are shown if present.
 *   @see <img src="https://raw.githubusercontent.com/Ahtat204/Gitlab/refs/heads/main/personalprojects.jpg"  width="300" height="700"/>
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalProjects(
    navController: NavHostController,
    x: PaddingValues,
    personalProjectsViewModel: PersonalProjectsViewModel = hiltViewModel()
) {
    LaunchedEffect(1) {
        personalProjectsViewModel.loadAllProjects()
    }
    val currUser by personalProjectsViewModel.projects.collectAsState()
    currUser?.namespace?.projects?.nodes?.sortedByDescending {
        Instant.parse(it?.lastActivityAt.toString()).atZone(ZoneId.systemDefault())
            .toLocalDate()
    }?.let { nodes ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(x)
                .background(Color.Black)
        ) {
            if (currUser?.namespace?.projects?.nodes?.isEmpty() == true || currUser?.avatarUrl == null) {
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
                        item?.let { ProjectItem(currUser, it, loader, navController) }
                    }
                }
            }
        }
    }
}