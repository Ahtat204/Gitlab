package com.asue24.gitlab.presentation.components

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
import com.asue24.gitlab.R
import com.asue24.gitlab.presentation.ui.theme.titleFont

/**
 * this Component shows a List of Work Items on your Gitlab , examples: MRs , Issues ,To-do items,Starred Projects...
 */
@Composable
fun MyWorkItems(navController: NavController) {
    val myWorkItems = listOf(
        Item("Issues", R.drawable.issues, 1),
        Item("Merge Requests ", R.drawable.mergerequest, 1),
        Item("Workspaces", R.drawable.workspaces, 1),
        Item("Milestones", R.drawable.milestone, 1),
        Item("Starred", R.drawable.star, 1),
        Item("Groups", R.drawable.group, 1)
    )

    Column(
        modifier = Modifier.padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Work", fontFamily = titleFont, fontSize = 20.sp)
        myWorkItems.forEach { item ->
            WorkItem(item) {
                navController.navigate(item)
            }
        }
    }
}