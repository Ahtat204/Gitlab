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
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Timer
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
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.presentation.ui.theme.Background
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily

typealias Pipeline = GetProjectPipelinesQuery.Node?

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pipeline(pipeline: Pipeline) {
    if (pipeline == null) return
    val status = pipeline.status
    val duration = pipeline.duration ?: return
    val finishedAt = iso8601ToRelative(pipeline.finishedAt as String)
    val user = pipeline.user?.name
    val branch = pipeline.ref ?: return
    val formatedDuration = formatRelative(duration.toLong(), true)
    val length = branch.length + 90
    val trigger = pipeline.type
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
                PipeLineStatusIcon(status)
                Text(
                    text = "${pipeline.commit?.name}",
                    maxLines = 1,
                    fontSize = 17.sp,
                    color = White,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(290.dp),
                    fontFamily = customFontFamily,
                )
                Text(
                    text = "via merge request $trigger by $user",
                    maxLines = 1,
                    fontSize = 10.sp,
                    color = White,
                    modifier = Modifier
                        .offset(0.dp, (10).dp)
                        .fillMaxWidth(0.8f),
                    fontFamily = customFontFamily,
                )
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = 15.dp)
                ) {
                    //duration tag
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Orange)
                            .height(15.dp)
                            .width(74.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Timer,
                            contentDescription = null,
                            Modifier
                                .size(12.dp)
                                .offset(x = 7.dp, y = 1.dp)
                        )
                        Text(
                            text = formatedDuration,
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = White,
                            modifier = Modifier
                                .offset(10.dp, y = (-3).dp),
                            fontFamily = customFontFamily,
                        )
                    }
                    //branch tag
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Orange)
                            .height(15.dp)
                            .width(length.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.branch),
                            contentDescription = null,
                            Modifier
                                .size(10.dp)
                                .offset(x = 7.dp, y = 1.dp)
                        )
                        Text(
                            text = branch,
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = White,
                            modifier = Modifier.offset(10.dp, y = (-3).dp),
                            fontFamily = customFontFamily,
                        )
                    }
                    //finishedAt tag
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Orange)
                            .height(15.dp)
                            .width(70.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = null,
                            Modifier
                                .size(10.dp)
                                .offset(x = 7.dp, y = 2.dp)
                        )
                        Text(
                            text = finishedAt,
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = White,
                            modifier = Modifier.offset(10.dp, y = (-3).dp),
                            fontFamily = customFontFamily,
                        )
                    }

                }
            }
        }
    }
}