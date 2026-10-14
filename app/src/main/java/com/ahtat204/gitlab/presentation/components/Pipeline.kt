package com.ahtat204.gitlab.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.zIndex
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.presentation.activities.ui.theme.Background
import com.ahtat204.gitlab.presentation.activities.ui.theme.Orange
import com.ahtat204.gitlab.presentation.activities.ui.theme.customFontFamily


typealias Pipeline = GetProjectPipelinesQuery.Node?

/**
 * A composable card component that displays detailed summary information for a single GitLab CI/CD pipeline.
 *
 * It extracts, formats, and renders pipeline characteristics such as its status, associated commit name,
 * execution duration, branch reference, trigger type, and execution completion timestamp. It early-returns
 * and renders nothing if the [pipeline] input model, its internal duration, or its branch reference is missing.
 *
 * ### UI Composition & Layout:
 * - **Status Indicator:** Shows an icon reflecting the current pipeline phase or final state using [PipeLineStatusIcon].
 * - **Header Metadata:** Displays the commit message alongside contextual strings detailing the trigger method and author.
 * - **Badges:** Uses distinct UI chips/tags wrapped with a custom dynamic width utility (`measureTextWidth`) to isolate:
 *   1. **Duration:** Formatted length of time elapsed during the run.
 *   2. **Branch/Ref:** Source control reference name with a graphic indicator.
 *   3. **Completion Time:** Human-readable relative string derived from an ISO-8601 representation.
 *
 * @param pipeline The nullable data transfer node wrapping target metrics extracted via a GraphQL project query.
 *
 * @see PipeLineStatusIcon
 * @see Pipeline
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pipeline(pipeline: Pipeline) {
    if (pipeline == null) return
    val status = pipeline.status
    val name = pipeline.commit?.name ?: ""
    val duration = pipeline.duration ?: return
    val finishedAt = iso8601ToRelative(pipeline.finishedAt as String)
    val user = pipeline.user?.name
    val ref = pipeline.ref ?: return
    val branch = if (ref.startsWith("refs")) pipeline.mergeRequest?.title else ref
    val formatedDuration = formatRelative(duration.toLong(), true)
    val trigger = pipeline.type
    branch?.let {
        Card(
            {}, modifier = Modifier
                .height(120.dp)
                .fillMaxSize()
                .padding(0.dp, 10.dp)
                .background(Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = 50.dp, y = (-20).dp)
                        .zIndex(10.0f)
                ) { PipeLineStatusIcon(status) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight()
                        .background(Background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = name,
                        maxLines = 1,
                        fontSize = 17.sp,
                        color = White,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(290.dp),
                        fontFamily = customFontFamily,
                    )
                    Text(
                        text = "via  $trigger by $user",
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
                        val durationTagWidth = measureTextWidth(formatedDuration)
                        val branchTagWidth = measureTextWidth(ref)
                        //duration tag
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Orange)
                                .height(15.dp)

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
                                    .offset(10.dp, y = (-3).dp)
                                    .width(durationTagWidth),
                                fontFamily = customFontFamily,
                            )
                        }
                        //branch tag
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .wrapContentWidth(unbounded = true)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Orange)
                                .height(15.dp)

                        ) {
                            Icon(
                                painter = painterResource(R.drawable.branch),
                                contentDescription = null,
                                Modifier
                                    .size(10.dp)
                                    .offset(x = 7.dp, y = 2.dp)
                            )
                            Text(
                                text = branch,
                                maxLines = 1,
                                fontSize = 10.sp,
                                color = White,
                                modifier = Modifier
                                    .offset(10.dp, y = (-3).dp)
                                    .width(branchTagWidth + 10.dp),
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
                                .width(measureTextWidth(finishedAt))
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

}