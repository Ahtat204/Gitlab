package com.ahtat204.gitlab.presentation.screens.project

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.activities.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.components.IssueCard
import com.ahtat204.gitlab.presentation.viewmodels.project.IssuesViewModel

/**
 * A composable screen that displays a list of issues associated with a specific project.
 *
 * This screen fetches and presents project issues using the provided [issuesViewModel].
 * It manages the lifecycle-aware collection of data and renders a scrollable list of
 * issue cards if data is present.
 *
 * @param navController The [NavHostController] used to handle navigation actions from this screen.
 * @param x The [PaddingValues] passed from a parent layout (such as a Scaffold) to avoid UI overlap.
 * @param project The identifier or name of the project whose issues are being fetched.
 * @param issuesViewModel The state holder for managing issue operations, defaulting to a Hilt-injected instance.
 *
 * @see IssueCard
 * @see IssuesViewModel
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Issues(
    navController: NavHostController,
    x: PaddingValues,
    project: String,
    issuesViewModel: IssuesViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        issuesViewModel.loadProjectIssues(project)
    }
    val issues by issuesViewModel.issues.collectAsStateWithLifecycle()
    val nodes = issues?.issues?.nodes
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(x)
    ) {

        Row(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Issues",
                fontFamily = titleFont,
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.5f)
                    .offset(20.dp, 0.dp)
            )
            IconButton(
                onClick = { },
                modifier = Modifier.weight(0.1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
            }
        }
        if (nodes?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState
            ) {
                items(nodes, key = { item -> item?.id ?: Any() }) { item ->
                    item?.let { IssueCard(it, navController = navController) }
                }
            }
        }
    }
}