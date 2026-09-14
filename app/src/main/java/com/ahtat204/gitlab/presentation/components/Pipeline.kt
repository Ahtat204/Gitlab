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
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.navigation.NavController
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.presentation.ui.theme.Background
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

typealias Pipeline = GetProjectPipelinesQuery.Node?

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pipeline(project: String, pipeline: Pipeline, navController: NavController) {
    if (pipeline == null) return
    val status = pipeline.status
    val duration = pipeline.duration ?: return
    val finishedAt = iso8601ToRelative(pipeline.finishedAt as String)
    val user = pipeline.user?.name
    val branch = pipeline.ref ?: return
    val formatedDuration = formatRelative(duration.toLong(), true)

    val trigger = pipeline.type
    val encodedProjectId = URLEncoder.encode(project, StandardCharsets.UTF_8.toString())
    val encodedPipelineId = URLEncoder.encode(pipeline.id, StandardCharsets.UTF_8.toString())
    Card(
        { navController.navigate("${encodedProjectId}/pipeline/${encodedPipelineId}") },
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
                    .weight(1.0f)
                    .background(Background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "${pipeline.commit?.name}",
                    maxLines = 1,
                    fontSize = 17.sp,
                    color = White,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(310.dp),
                    fontFamily = customFontFamily,
                )
                Text(
                    text = "via $trigger by $user",
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                    val isBranch = trigger == "branch"
                    val number = if (isBranch) branch else Regex("[0-9]+").findAll(
                        branch
                    ).map { it.value.toInt() }.firstOrNull().toString()
                    //val brackets = """/""".toRegex().findAll(branch).toList().size
                    //branch tag
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .wrapContentWidth()
                            //  .fillMaxWidth(length.toFloat())
                            .clip(RoundedCornerShape(20.dp))
                            .background(Orange)
                            .height(15.dp)


                    ) {
                        Icon(
                            painter = painterResource(if (isBranch) R.drawable.branch else R.drawable.mergerequest),
                            contentDescription = null,
                            Modifier
                                .size(10.dp)
                                .offset(x = 7.dp, y = 1.dp)
                            //    .weight(0.05f)
                        )

                        Text(
                            text = number,
                            maxLines = 1,
                            fontSize = 10.sp,
                            color = White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(10.dp, y = (-3).dp),
                            //.fillMaxWidth(0.5f)
                            //    .weight(0.8f)
                            fontFamily = customFontFamily,
                        )

                    }

                }
            }
            PipeLineStatusIcon(
                status, Modifier
                    .weight(0.1f)
            )
        }
    }
}