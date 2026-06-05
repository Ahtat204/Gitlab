package com.ahtat204.gitlab.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ahtat204.gitlab.presentation.components.ProjectItem
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.ProjectViewModel
import java.time.Instant
import java.time.ZoneId

/**
 * the x:Paddingvalues parameter will be injected from the Scaffold
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalProjects(x: PaddingValues, projectViewModel: ProjectViewModel = hiltViewModel()) {
    LaunchedEffect(1) {
        projectViewModel.loadAllProjects()
    }
    val CurrUser by projectViewModel.projects.collectAsState()
    CurrUser?.projectMemberships?.nodes?.sortedBy {
        Instant.parse(it?.project?.lastActivityAt.toString()).atZone(ZoneId.systemDefault())
            .toLocalDate()
    }?.let {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(x)
                .background(Color.Black)
        ) {
            if (CurrUser?.projectMemberships?.nodes?.isEmpty() == true || CurrUser?.avatarUrl == null) {
                CircularProgressIndicator(modifier = Modifier.offset(160.dp, y = (190).dp))

            } else {
                Text(
                    text = "Your Projects",
                    fontFamily = titleFont,
                    fontSize = 20.sp,
                    modifier = Modifier
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = x,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(it) { item ->
                        item?.project?.let { ProjectItem(CurrUser, it) }
                    }
                }
            }
        }
    }
}