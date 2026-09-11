package com.ahtat204.gitlab.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.project.MembersViewModel

@Composable
fun Members(
    project: String,
    navController: NavController,
    x: PaddingValues,
    viewModel: MembersViewModel = hiltViewModel()
) {
    if (project.isEmpty()) return
    val members by viewModel.members.collectAsStateWithLifecycle()
    LaunchedEffect(project) {
        viewModel.loadProjectMembers(project)
    }
    val nodes = members?.projectMembers?.nodes
    if (nodes?.isEmpty() == true) return
    val listState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Trigger load when user is 3 items away from the bottom
            totalItems > 9 && lastVisibleItem >= totalItems - 9
        }
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.loadProjectMembers(project)
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(x)
    ) {
        //  Text(text = "Members", fontFamily = titleFont, fontSize = 20.sp, color = Orange)
        nodes?.let { nodes ->

            if (nodes.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Members",
                        fontFamily = titleFont,
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(0.5f)
                            .offset(20.dp, 0.dp)
                    )
                    IconButton(
                        onClick = { viewModel.refreshMembers(project) },
                        modifier = Modifier.weight(0.1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        items = nodes,
                        key = { item ->
                            item?.id ?: item?.user?.name ?: null.hashCode()
                        }) { member ->
                        Card(
                            {}, modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp, 10.dp)
                                .background(Color.Black)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .height(80.dp)
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.background(Color(0xFF000000)),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = member?.user?.name ?: "",
                                        fontFamily = customFontFamily,
                                        modifier = Modifier.fillMaxWidth(0.7f),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 2,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = member?.user?.username ?: "",
                                        fontFamily = titleFont,
                                        fontSize = 12.sp,
                                        color = Orange,
                                        modifier = Modifier.fillMaxWidth(0.6f),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 2,

                                        )
                                    Text(
                                        text = member?.user?.bio ?: "",
                                        fontFamily = titleFont,
                                        fontSize = 12.sp,
                                        color = Orange,
                                        modifier = Modifier.fillMaxWidth(0.9f),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 2
                                    )
                                }

                                Text(
                                    text = "",
                                    fontFamily = customFontFamily,
                                    modifier = Modifier,
                                    color = Orange
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}