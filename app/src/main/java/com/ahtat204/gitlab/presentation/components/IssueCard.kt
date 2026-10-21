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
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.GetProjectIssuesQuery
import com.ahtat204.gitlab.presentation.activities.ui.theme.Background
import com.ahtat204.gitlab.presentation.activities.ui.theme.Orange
import com.ahtat204.gitlab.presentation.activities.ui.theme.customFontFamily

typealias Issue = GetProjectIssuesQuery.Node?

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IssueCard(issue: Issue, navController: NavHostController) {
    issue?.let { node ->
        val name = node.name
        val state = node.state
        val title = node.title
        val firstAssigne = node.assignees?.nodes?.firstOrNull()
        val iid = node.iid
        val date = iso8601ToRelative(node.createdAt as String)
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
                            painter = painterResource(R.drawable.issues),
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(10.dp)) // Clip first

                        )

                        Text(
                            text = "$name",
                            fontSize = 17.sp,
                            color = White,
                            overflow = TextOverflow.Ellipsis,
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
///////////////////////////
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = 15.dp)
                    ) {
                        //iid tag
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .weight(0.2f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Orange)
                                .height(15.dp)
                                .width(20.dp)
                        ) {

                            Text(
                                text = "#$iid",
                                maxLines = 1,
                                fontSize = 10.sp,
                                color = White,
                                modifier = Modifier.offset(10.dp, y = (-3).dp),
                                fontFamily = customFontFamily,
                            )
                        }
                        //state tag
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .weight(0.2f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Orange)
                                .height(15.dp)
                                .width(45.dp)
                        ) {

                            Text(
                                text = state.name,
                                maxLines = 1,
                                fontSize = 10.sp,
                                color = White,
                                modifier = Modifier.offset(10.dp, y = (-3).dp),
                                fontFamily = customFontFamily,
                                textAlign = TextAlign.Start
                            )
                        }
                        firstAssigne?.let {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .weight(0.3f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Orange)
                                    .height(15.dp)
                                    .width(55.dp)
                            ) {
                                Text(
                                    text = it.name,
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