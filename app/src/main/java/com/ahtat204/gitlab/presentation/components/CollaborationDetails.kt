package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahtat204.gitlab.R

@Composable
fun CollaborationDetails(
    issuesCount: Int,
    mergeRequestCount: Int,
    membersCount: Int,
    pipelinesCount: Int,
    workItemsCount: Int,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProjectWorkItems(Item("issues", "issues", R.drawable.issues, issuesCount)) {}
        ProjectWorkItems(Item("Merge Requests", "issues", R.drawable.issues, mergeRequestCount)) {}
        ProjectWorkItems(Item("Pipelines", "issues", R.drawable.issues, membersCount)) {}
        ProjectWorkItems(Item("WorkItems", "issues", R.drawable.issues, pipelinesCount)) {}
        ProjectWorkItems(Item("Members", "issues", R.drawable.issues, workItemsCount)) {}
    }

}