package com.ahtat204.gitlab.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.presentation.activities.ui.theme.Background
import com.ahtat204.gitlab.presentation.activities.ui.theme.Orange
import com.ahtat204.gitlab.presentation.activities.ui.theme.customFontFamily

typealias MergeRequest = GetProjectMergeRequestsQuery.Node?

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MergeRequest(mr: MergeRequest, navController: NavHostController) {
    mr?.let { node ->
        val label = node.labels?.nodes?.firstOrNull()
        val author = node.author?.name
        val sourceBranch = node.sourceBranch
        val date = iso8601ToRelative(node.createdAt as String)
        Card(
            {}, modifier = Modifier
                .height(100.dp)

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
                    verticalArrangement = Arrangement.Top,
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
                            text = "${node.name}\t#${node.iid} ",
                            maxLines = 2,
                            fontSize = 17.sp,
                            color = White,
                            overflow = TextOverflow.Clip,
                            modifier = Modifier
                                .width(270.dp)
                                .padding(horizontal = 10.dp),
                            fontFamily = customFontFamily,
                        )
                        Text(
                            text = date,
                            maxLines = 1,
                            fontSize = 17.sp,
                            color = White,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .width(290.dp)
                                .padding(horizontal = 0.dp),
                            fontFamily = customFontFamily,
                        )

                    }

                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = 15.dp)
                    ) {
                        //author tag
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Orange)
                                .height(15.dp)
                                .width(measureTextWidth(author ?: "") + 20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                Modifier
                                    .size(12.dp)
                                    .offset(x = 7.dp, y = 1.dp)
                            )
                            Text(
                                text = author ?: "",
                                maxLines = 1,
                                fontSize = 10.sp,
                                color = White,
                                modifier = Modifier.offset(10.dp, y = (-3).dp),
                                fontFamily = customFontFamily,
                            )
                        }
                        //Source branch tag
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Orange)
                                .height(15.dp)
                                .width(measureTextWidth(sourceBranch) - 25.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.branch),
                                contentDescription = null,
                                Modifier
                                    .size(10.dp)
                                    .offset(x = 7.dp, y = 1.dp)
                            )
                            Text(
                                text = sourceBranch,
                                maxLines = 1,
                                fontSize = 10.sp,
                                color = White,
                                modifier = Modifier.offset(10.dp, y = (-3).dp),
                                fontFamily = customFontFamily,
                                textAlign = TextAlign.Start
                            )
                        }
                        label?.let {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(it.color.toColorInt()))
                                    .height(15.dp)
                                    .width(measureTextWidth(sourceBranch) - 55.dp)
                            ) {
                                Text(
                                    text = it.title,
                                    maxLines = 1,
                                    fontSize = 10.sp,
                                    color = White,
                                    modifier = Modifier.offset(10.dp, y = (-3).dp),
                                    fontFamily = customFontFamily,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}