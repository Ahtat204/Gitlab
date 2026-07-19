package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Displays the header section for a repository view.
 *
 * ## Purpose
 * - Provides a compact summary of the repository’s current branch, latest commit message, and timeline.
 * - Includes interactive buttons for branch selection and commit history navigation.
 *
 * ## Parameters
 * @param showSheet Callback invoked when the branch selector button is clicked.
 *                  Typically toggles a modal bottom sheet for branch selection.
 * @param currentBranch A [MutableState] holding the currently selected branch name.
 * @param message The latest commit message to display. Truncated if too long.
 * @param name The author of the latest commit. May be `null`.
 * @param parsedDateTime A relative timestamp string (e.g., "2 hours ago").
 * @param navController Navigation controller used to navigate to the commit history screen.
 * @param projectPath The unique path of the project whose repository is being displayed.
 * @param showHistory lambda to clear the LinkedHashMap
 *
 * ## Behavior
 * - Left section: A [TextButton] showing the branch icon and current branch name.
 *   - Clicking toggles the branch selection sheet via [showSheet].
 * - Middle section: A [Column] displaying:
 *   - Commit message (max 2 lines, ellipsized).
 *   - Timeline string combining author name and relative commit time.
 * - Right section: A [TextButton] labeled "history" that navigates to the commits screen.
 *   - Uses URL-encoded project path and branch name for safe routing.
 *
 * ## Layout
 * - Root: [Row] with black background, rounded border, and fixed height.
 * - Branch section: [Icon] + branch name text.
 * - Commit section: [Column] with commit message and timeline.
 * - History section: [TextButton] with "history" label.
 *
 * ## Example
 * ```
 * RepositoryHead(
 *     showSheet = { showBranchSheet = true },
 *     currentBranch = remember { mutableStateOf("main") },
 *     message = "Fix bug in authentication flow",
 *     name = "Alice",
 *     parsedDateTime = "3 hours ago",
 *     navController = navController,
 *     projectPath = "my-group/my-project"
 * )
 * ```
 *
 * ## Notes
 * - Branch names and project paths are URL-encoded before navigation.
 * - Text elements use [customFontFamily] for consistent styling.
 * - Ensure [navController] is properly configured with a "commits/{id}/{branch}" route.
 */
@Composable
fun RepositoryHead(
    showSheet: () -> Unit,
    currentBranch: MutableState<String?>,
    message: String,
    name: String?,
    parsedDateTime: String,
    navController: NavController,
    projectPath: String,
    showHistory : MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF000000))
            .fillMaxWidth()
            .border(
                width = (0.1f).dp, color = Color(0xFF675353), shape = RoundedCornerShape(10.dp)
            )
            .height(75.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = showSheet
        ) {
            Icon(
                painter = painterResource(R.drawable.branch),
                contentDescription = "branch",
                Modifier
                    .size(25.dp)
                    .padding(3.dp),
                tint = Orange
            )
            Text(
                text = currentBranch.value ?: "",
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
                text = "$name authored $parsedDateTime",
                maxLines = 1,
                fontSize = 10.sp,
                color = White,
                modifier = Modifier
                    .offset(0.dp, (10).dp)
                    .fillMaxWidth(0.8f),
                fontFamily = customFontFamily,
            )
        }
        val encodedId = URLEncoder.encode(projectPath, StandardCharsets.UTF_8.toString())
        val encodedBranch = URLEncoder.encode(
            currentBranch.value, StandardCharsets.UTF_8.toString()
        )
        TextButton(onClick = {
            //
            showHistory.value= !showHistory.value
        }) {
            Text(
                text = if(showHistory.value==false)"history" else "browser",
                fontSize = 15.sp,
                color = White,
                modifier = Modifier,
                fontFamily = customFontFamily,
            )
        }
    }
}