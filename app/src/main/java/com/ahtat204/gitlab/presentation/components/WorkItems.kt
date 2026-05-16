package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
 * this Component shows a List of Work Items on your Gitlab , examples: MRs , Issues ,To-do items,Starred Projects...
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