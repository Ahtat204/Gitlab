package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.data.queries.GetProjectWorkItemsQuery
import com.ahtat204.gitlab.presentation.activities.ui.theme.Background

typealias WorkItem = GetProjectWorkItemsQuery.Node?

@Composable
fun GitlabWorkItem(workItem: WorkItem) {
    workItem?.let {
        val name = it.name ?: return
        val title = it.title
        val state = it.state
        val createdAt = it.createdAt as String

        Card(
            {}, modifier = Modifier
                .height(120.dp)
                .fillMaxSize()
                .padding(10.dp, 10.dp)
                .background(Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = name)
                    Text(text = title)
                    Text(text = createdAt)

                }
            }
        }
    }

}