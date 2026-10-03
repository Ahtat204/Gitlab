package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.presentation.activities.ui.theme.Background
import com.ahtat204.gitlab.presentation.activities.ui.theme.Orange
import com.ahtat204.gitlab.presentation.activities.ui.theme.customFontFamily

typealias MergeRequest = GetProjectMergeRequestsQuery.Node?

@Composable
fun MergeRequest(mr: MergeRequest, navController: NavHostController) {

    mr?.let { node ->
        val labels = node.labels
        val author = node.author?.name
        val id = node.id
        Card(
            {},
            modifier = Modifier
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.mergerequest),
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(10.dp)) // Clip first

                        )

                        Text(
                            text = node.name ?: "",
                            maxLines = 1,
                            fontSize = 17.sp,
                            color = White,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(290.dp)
                                .padding(horizontal = 10.dp),
                            fontFamily = customFontFamily,
                        )
                    }
                }
                Text(
                    text = "created by $author",
                    maxLines = 1,
                    fontSize = 10.sp,
                    color = White,
                    modifier = Modifier
                        .offset(0.dp, (10).dp)
                        .fillMaxWidth(0.8f),
                    fontFamily = customFontFamily,
                )
            }
        }
    }
}