package com.ahtat204.gitlab.presentation.screens.project

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.components.FileExplorer
import com.ahtat204.gitlab.presentation.components.RepositoryHead
import com.ahtat204.gitlab.presentation.components.iso8601ToRelative
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.viewmodels.RepositoryViewModel
import kotlinx.coroutines.launch

/**
 * Displays the repository screen for a given project.
 *
 * ## Purpose
 * - Provides a UI for exploring a project’s repository contents.
 * - Shows the repository header with branch, commit message, and timeline.
 * - Renders the repository tree via [FileExplorer].
 *
 * ## Parameters
 * @param projectPath The unique path of the project whose repository should be displayed.
 * @param x Padding values applied to the screen layout.
 * @param repositoryViewModel ViewModel responsible for loading and exposing repository data.
 * Defaults to [hiltViewModel] injection.
 *
 * ## Behavior
 * - On first composition, triggers [RepositoryViewModel.loadProjectRepository] with [projectPath].
 * - Observes repository state via [collectAsStateWithLifecycle].
 * - If repository data is available:
 *   - Displays [RepositoryHead] with commit message, author, timeline, and branch reference.
 *   - Displays [FileExplorer] with the repository tree.
 * - Dates are formatted using [iso8601ToRelative] for relative time display (e.g., "2 hours ago").
 *
 * ## Layout
 * - Root: [Column] with black background, applied padding, and full height.
 * - Top section: [RepositoryHead] showing branch and commit info.
 * - Bottom section: [FileExplorer] rendering the repository tree.
 *
 * ## Example
 * ```
 * RepositoryScreen(
 *     projectPath = "my-group/my-project",
 *     x = PaddingValues(16.dp)
 * )
 * ```
 *
 * ## Notes
 * - Requires API level [Build.VERSION_CODES.O] for date formatting.
 * - The timeline string combines author name and relative commit time.
 * - Ensure [RepositoryViewModel] is properly provided via Hilt for dependency injection.
 *  @see <img src="https://raw.githubusercontent.com/Ahtat204/Gitlab/refs/heads/screen/project/repository/repository.jpg"  width="300" height="700"/>
 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepositoryScreen(
    projectPath: String,
    x: PaddingValues,
    repositoryViewModel: RepositoryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }
    val offsetY = remember { Animatable(1000f) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    var isBranchListVisible by remember { mutableStateOf(false) }
    val branch = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(branch.value) {
        repositoryViewModel.loadProjectRepository(projectPath, branch.value)
        repositoryViewModel.loadRepositoryBranches(projectPath)
    }
    LaunchedEffect(isVisible) {

    }
    val repository by repositoryViewModel.repository.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(x)
            .fillMaxHeight()
            .clickable(onClick = { isBranchListVisible = false })
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


     /*     if (showSheet) {
        ModalBottomSheet(modifier = Modifier.fillMaxHeight(),
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Button(
                onClick = {
                    scope.launch { sheetState.hide() }
                    showSheet = false
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Close")
            }
        }
    }*/
        repository?.lastCommit?.message?.let { message ->
            repository?.rootRef?.let { rootRef ->
                repository?.lastCommit?.committedDate.let { date ->
                    val parsedDateTime = iso8601ToRelative(date as String)

                    if (branch.value == null) branch.value = rootRef
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF000000))
                            .fillMaxWidth()
                            .border(
                                width = (0.1f).dp,
                                color = Color(0xFF675353),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .height(75.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = {
                            isVisible = !isVisible
                            scope.launch {
                                offsetY.animateTo(0f, tween(300)) }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.branch),
                                contentDescription = "branch",
                                Modifier
                                    .size(25.dp)
                                    .padding(3.dp),
                                tint = Orange
                            )
                            Text(
                                text = branch.value ?: "",
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 15.sp,
                                color = White,
                                modifier = Modifier.width(60.dp),
                                fontFamily = customFontFamily,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF000000))
                                .weight(0.5f)
                                .fillMaxHeight()
                                .fillMaxWidth(0.75f),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = message,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = White,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .offset(0.dp, 10.dp),
                                fontFamily = customFontFamily,
                            )

                            Text(
                                text = "${repository?.lastCommit?.author?.name} authored $parsedDateTime",
                                maxLines = 1,
                                fontSize = 10.sp,
                                color = White,
                                modifier = Modifier
                                    .offset(0.dp, (10).dp)
                                    .fillMaxWidth(0.8f),
                                fontFamily = customFontFamily,
                            )
                        }
                        TextButton(onClick = {}) {
                            Text(
                                text = "history",
                                fontSize = 15.sp,
                                color = White,
                                modifier = Modifier,
                                fontFamily = customFontFamily,
                            )
                        }
                    }/* RepositoryHead(
                        commitMessage = message,
                        timeline = "${repository?.lastCommit?.author?.name} authored $parsedDateTime",
                        branch = branch
                    )*/
                }/*   */
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        repository?.tree?.let {
            FileExplorer(it)
        }
    }
       Box(modifier = Modifier.fillMaxSize()) {
        if (isVisible) {
            // click anywhere in the box to slide the panel down
         /*   Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan.copy(alpha = 0.0f))
                    .clickable {
                        scope.launch {
                            offsetY.animateTo(30f, tween(20))
                            isVisible = false
                        }
                    }
            )*/
            Box(
                modifier = Modifier.clip(RoundedCornerShape(30.dp))
                    .align(Alignment.BottomCenter)
                    .offset(y = offsetY.value.dp)
                    .fillMaxWidth().height(550.dp)
                    .background(Color.DarkGray)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val newOffset = (offsetY.value + dragAmount.y).coerceAtLeast(0f)
                                scope.launch { offsetY.snapTo(newOffset) }
                            },
                            onDragEnd = {
                                scope.launch {
                                    if (offsetY.value > 150f) {
                                        offsetY.animateTo(1000f, tween(1300))
                                        isVisible = false
                                    } else {
                                        offsetY.animateTo(0f, tween(1200))
                                    }
                                }
                            }
                        )
                    }
            ) {
                val branches by repositoryViewModel.branches.collectAsStateWithLifecycle()
                branches?.branchNames?.let { branches
                    LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = x,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    items(it){
                        Text(it)
                    }
                }}


            }
        }
    }






}