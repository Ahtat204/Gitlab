package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asue24.gitlab.GetMyProjectsQuery

@Composable
fun ProjectItem(project: GetMyProjectsQuery.Node) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            project.project?.name?.let {
                Text(
                    text = it, fontSize = 18.sp, modifier = Modifier.weight(1f)
                )
            }
        }
    }
}