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
    issuesCount: Int?,
    mergeRequestCount: Int?,
    membersCount: Int?,
    pipelinesCount: Int?,
    workItemsCount: Int?,
    commitCount: Double?,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(0.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        issuesCount.let { ProjectWorkItems(Item("issues", "issues", R.drawable.issues, it)) {} }
        mergeRequestCount?.let {
            ProjectWorkItems(
                Item(
                    "Merge Requests", "merge_requests", R.drawable.mergerequest, it
                )
            ) {}
        }
        membersCount?.let {
            ProjectWorkItems(
                Item(
                    "Pipelines", "project/{id}/pipelines", R.drawable.pipeline, it
                )
            ) {}
        }
        pipelinesCount?.let {
            ProjectWorkItems(
                Item(
                    "WorkItems", "project/{id]/workitems", R.drawable.workitems, it
                )
            ) {}
        }
        workItemsCount?.let {
            ProjectWorkItems(
                Item(
                    "Members", "project/{id}/members", R.drawable.members, it
                )
            ) {}
        }

        commitCount?.let {
            ProjectWorkItems(
                Item(
                    "Commits", "project/{id}/commits", R.drawable.commit, it.toInt()
                )
            ) {}
        }
    }

}