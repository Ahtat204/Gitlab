package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                , verticalArrangement = Arrangement.Center
        ) {
            data.project?.let { project ->
                Text(
                    text = project.name,
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier,
                    fontFamily = customFontFamily
                )
                project.description?.let {
                    Text(
                        text = it,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 9.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        fontFamily = customFontFamily,
                    )
                }
            }
        }
    }
}