package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.data.queries.GetProjectMergeRequestsQuery
import com.ahtat204.gitlab.presentation.ui.theme.Background
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily

@Composable
fun MergeRequest(node: GetProjectMergeRequestsQuery.Node, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { })
            .fillMaxHeight()
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Background)
            ) {
                node.let { mr ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        mr.name?.let {
                            Text(
                                text = it,
                                fontSize = 17.sp,
                                color = Color.White,
                                modifier = Modifier.offset(10.dp, 0.dp),
                                fontFamily = customFontFamily
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            }
        }
    }
}