package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahtat204.gitlab.R

/**
 * Displays a summary of collaboration-related metrics for a GitLab project.
 *
 * This composable arranges multiple [ProjectWorkItems] in a vertical column,
 * each representing a specific aspect of project collaboration such as issues,
 * merge requests, members, pipelines, work items, and commits.
 *
 * ## Purpose
 * - Provides a quick overview of project activity and participation.
 * - Each metric is represented by an [Item] with an icon, label, and count.
 * - Designed to be used within a project details screen.
 *
 * ## Parameters
 * @param issuesCount Number of issues in the project (nullable).
 * @param mergeRequestCount Number of merge requests (nullable).
 * @param pipelinesCount Number of pipelines (nullable).
 * @param navController Navigation controller used for handling navigation actions.
 *
 * ## Behavior
 * - Each non-null parameter is rendered as a [ProjectWorkItems] entry.
 * - Entries are displayed in a vertical [Column] with centered alignment.
 * - Background is styled with [Color.Black].
 *
 * ## Example
 * ```
 * CollaborationDetails(
 *     issuesCount = 42,
 *     mergeRequestCount = 10,
 *     pipelinesCount = 3,
 *     navController = navController
 * )
 * ```
 * @author Lahcen AHTAT
 */
@Composable
fun CollaborationDetails(
    issuesCount: Int?,
    mergeRequestCount: Int?,
    pipelinesCount: Int?,
    navController: NavController,
    encodedId:String
) {
    Column(
        modifier = Modifier
            .padding(0.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProjectWorkItems(Item("issues", "issues", R.drawable.issues, issuesCount)) {
            navController.navigate("issues?projectId=$encodedId")
        }
        ProjectWorkItems(
            Item(
                "Merge Requests", "merge_requests", R.drawable.mergerequest, mergeRequestCount
            )
        ) {}
            ProjectWorkItems(
                Item(
                    "Pipelines", "project/{id}/pipelines", R.drawable.pipeline, null
                )
            ) {}
        ProjectWorkItems(
            Item(
                "WorkItems", "project/{id]/workitems", R.drawable.workitems, null
            )
        ) {}
            ProjectWorkItems(
                Item(
                    "Members", "project/{id}/members", R.drawable.members, null
                )
            ) {}
    }

}