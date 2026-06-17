package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.ImageLoader
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.components.CollaborationDetails
import com.ahtat204.gitlab.presentation.components.GeneralDetails
import com.ahtat204.gitlab.presentation.components.ProjectItem
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.ui.theme.topBarFont
import com.ahtat204.gitlab.presentation.viewmodels.ProjectViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneId

/**
 * Composable that displays the details of a given project.
 *
 * ## Overview
 * - Fetches and observes project data via [ProjectViewModel].
 * - Shows a loading indicator until projects data is available.
 * - Delegates rendering the project details to a set of Components.
 *
 * ## Parameters
 * @param x The [PaddingValues] applied to the container for spacing.
 * @param path the path of the project to show.
 * @param projectViewModel The [ProjectViewModel] instance used to load and observe project data. Defaults to Hilt‑provided instance via [hiltViewModel].
 *
 * ## UI Behavior
 * - Initializes a Coil [ImageLoader] with caching and crossfade enabled.
 * - Calls [ProjectViewModel.loadAllProjects] inside [LaunchedEffect] to trigger data fetch.
 * - Collects current user data from [ProjectViewModel.projects] as state.
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
 */
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    x: PaddingValues,
    path: String,
    projectViewModel: ProjectViewModel = hiltViewModel()
) {
    val project by projectViewModel.currentProject.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        projectViewModel.loadProject(path)
    }
    Column(
        modifier = Modifier
            .background(Color.Black)
            .padding(x)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        project?.let { pro ->
            val encodedId = URLEncoder.encode(pro.fullPath, StandardCharsets.UTF_8.toString())
            Text(
                text = pro.namespace?.path ?: "",
                fontFamily = titleFont,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
            GeneralDetails(
                pro.forksCount, pro.starCount, pro.name, pro.description ?: ""
            )
            CollaborationDetails(
                pro.openIssuesCount ?: 0,
                pro.openMergeRequestsCount ?: 0,
                pro.pipelineCounts?.running,
                navController,
            )

            Card(
                { navController.navigate("repository?projectId=$encodedId") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 1.dp)
                    .background(Color.Black)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .height(60.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.repository),
                        contentDescription = null,
                        Modifier
                            .size(27.dp)
                            .padding(0.dp, 3.dp),
                        tint = Orange
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Repository",
                        fontSize = 18.sp,
                        fontFamily = topBarFont,
                        modifier = Modifier
                            .weight(0.9f)
                            .padding(10.dp),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
