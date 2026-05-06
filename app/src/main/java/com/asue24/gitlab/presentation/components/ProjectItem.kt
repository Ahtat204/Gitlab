package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asue24.gitlab.GetMyProjectsQuery
import com.asue24.gitlab.presentation.ui.theme.Orange
import com.asue24.gitlab.presentation.ui.theme.customFontFamily

@Composable
fun ProjectItem(data: GetMyProjectsQuery.Node) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            data.project?.let { project ->
                Text(
                    text = project.name,
                    fontSize = 13.sp,
                    color = Orange,
                    modifier = Modifier,
                    fontFamily = customFontFamily
                )
                project.description?.let {
                    Text(
                        text = it,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 9.sp,
                        color = Orange,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        fontFamily = customFontFamily,

                    )
                }
            }
        }
    }
}