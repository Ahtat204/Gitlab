package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.ui.theme.titleFont

/**
 * Composable that displays a list of work items (issues, merge requests, groups, etc.)
 * in a vertical column with navigation support.
 *
 * ## Overview
 * - Renders a section titled **"Your Work"**.
 * - Displays a predefined list of [Item] objects representing GitLab work categories.
 * - Each item is rendered via [WorkItem] composable.
 * - Clicking an item triggers navigation to the corresponding route using [NavController].
 *
 * ## Parameters
 * @param navController The [NavController] used to handle navigation when a work item is selected.
 *
 * ## UI Behavior
 * - The list includes:
 *   - Issues
 *   - Merge Requests
 *   - Workspaces
 *   - Milestones
 *   - Starred projects
 *   - Groups
 *   - Personal projects
 *   - Contributed projects
 * - Each item has:
 *   - A title (e.g., "Issues")
 *   - A route string (e.g., "issues")
 *   - An icon resource (e.g., `R.drawable.issues`)
 *   - A priority or type indicator (currently set to `1` for all items).
 *
 * ## Example
 * ```kotlin
 * MyWorkItems(navController = rememberNavController())
 * ```
 *
 * ## Notes
 * - Navigation routes should be defined in the app’s navigation graph to match
 *   the `item.route` values.
 * - The [WorkItem] composable is responsible for rendering individual items.
 */
@Composable
fun MyWorkItems(navController: NavController) {
    val myWorkItems = listOf(
        Item("Issues", "issues",R.drawable.issues, 1),
        Item("Merge Requests ","mergerequests", R.drawable.mergerequest, 1),
        Item("Workspaces","workspaces", R.drawable.workspaces, 1),
        Item("Milestones","workspaces", R.drawable.milestone, 1),
        Item("Starred","starrted", R.drawable.star, 1),
        Item("Groups","groups", R.drawable.group, 1),
        Item("Personal projects", "personal",R.drawable.project, 1),
        Item("Contributed Projects", "contributed",R.drawable.project, 1),
    )

    Column(
        modifier = Modifier.padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Work", fontFamily = titleFont, fontSize = 20.sp)
        myWorkItems.forEach { item ->
            WorkItem(item) {
                navController.navigate(item.route)
            }
        }
    }
}