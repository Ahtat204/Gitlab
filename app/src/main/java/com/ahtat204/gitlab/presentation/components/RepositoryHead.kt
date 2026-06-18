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
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
/**
 * Displays the header section for a repository view.
 *
 * ## Purpose
 * - Provides a compact summary of the repository’s current branch, latest commit message, and timeline.
 * - Includes interactive buttons for branch display and commit history navigation.
 *
 * ## Parameters
 * @param commitMessage The latest commit message to display. Truncated if too long.
 * @param timeline A string representing the commit timeline (e.g., "2 hours ago").
 * @param branch The root reference (branch name) of the repository.
 *
 * ## Behavior
 * - Left section: A [TextButton] showing the branch icon and branch name.
 * - Middle section: A [Column] displaying:
 *   - Commit message (max 2 lines, ellipsized).
 *   - Timeline (single line, ellipsized).
 * - Right section: A [TextButton] labeled "history".
 * - Entire row styled with black background, rounded border, and fixed height.
 *
 * ## Layout
 * - Root: [Row] with centered vertical alignment and border styling.
 * - Branch section: [Icon] + branch name text.
 * - Commit section: [Column] with commit message and timeline.
 * - History section: [TextButton] with "history" label.
 *
 * ## Example
 * ```
 * RepositoryHead(
 *     commitMessage = "Fix bug in authentication flow",
 *     timeline = "3 hours ago",
 *     rootRef = "main"
 * )
 * ```
 *
 * ## Notes
 * - The branch and history buttons currently have empty click handlers (`onClick = {}`).
 *   Implement navigation or actions as needed.
 * - Text elements use [customFontFamily] for consistent styling.
 */
@Composable
fun RepositoryHead(commitMessage: String, timeline:String, branch: MutableState<String?>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF000000)).fillMaxWidth().border(
                width = (0.1f).dp, color = Color(0xFF675353), shape = RoundedCornerShape(10.dp)
            )
            .height(75.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {branch.value="feature/authenticationbranch"}) {

            Icon(
                painter = painterResource(R.drawable.branch),
                contentDescription = "branch",
                Modifier.size(25.dp).padding(3.dp),
                tint =Orange
            )
            Text(
                text = branch.value?:"",
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp,
                color = White,
                modifier = Modifier.width(60.dp),
                fontFamily = customFontFamily,
            )
        }
        Column (
            modifier = Modifier
                .background(Color(0xFF000000)).weight(0.5f)
                .fillMaxHeight().fillMaxWidth(0.75f),
            verticalArrangement =  Arrangement.Top
            , horizontalAlignment =Alignment.CenterHorizontally
        ) {
            Text(
                text = commitMessage,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = White,
                modifier = Modifier
                    .fillMaxWidth(0.8f).offset(0.dp,10.dp),
                fontFamily = customFontFamily,
            )

            Text(
                text = timeline,
                maxLines = 1,
                fontSize = 10.sp,
                color = White,
                modifier = Modifier.offset(0.dp,(10).dp).fillMaxWidth(0.8f),
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
    }
}