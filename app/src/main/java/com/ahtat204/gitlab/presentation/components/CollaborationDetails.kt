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
        modifier = Modifier
            .padding(0.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProjectWorkItems(Item("issues", "issues", R.drawable.issues, issuesCount)) {}
        ProjectWorkItems(
            Item(
                "Merge Requests",
                "merge_requests",
                R.drawable.mergerequest,
                mergeRequestCount
            )
        ) {}
        ProjectWorkItems(Item("Pipelines", "pipelines", R.drawable.pipeline, membersCount)) {}
        ProjectWorkItems(Item("WorkItems", "work_items", R.drawable.workitems, pipelinesCount)) {}
        ProjectWorkItems(Item("Members", "members", R.drawable.members, workItemsCount)) {}
    }

}