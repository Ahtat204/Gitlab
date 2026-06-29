package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.ui.theme.titleFont

/**
 * Displays a single commit entry inside a styled card.
 *
 * ## Purpose
 * - Provides a reusable UI component for showing commit details.
 * - Each card displays the commit message and SHA identifier.
 *
 * ## Parameters
 * @param sha The commit SHA string. If `null`, the card will not render content.
 * @param message The commit message. If `null`, the card will not render content.
 *
 * ## Behavior
 * - If either [sha] or [message] is `null`, the card exits early without rendering.
 * - Both values are displayed as [Text] elements inside a [Box].
 * - The card uses a black background and fixed height for consistency.
 *
 * ## Layout
 * - Root: [Card] with full width, black background, and padding (20.dp horizontal, 10.dp vertical).
 * - Inner container: [Box] with black background and height of 50.dp.
 * - Content: Two [Text] elements:
 *   - Commit message
 *   - Commit SHA
 * - Both texts use [customFontFamily] for styling.
 *
 * ## Example
 * ``` Kotlin
 * CommitCard(
 *     sha = "abc123def",
 *     message = "Fix bug in authentication flow"
 * )
 * ```
 *
 * ## Notes
 * - Consider adding spacing or alignment between the message and SHA for improved readability.
 * - Accessibility: Provide meaningful descriptions if commit details are critical for screen readers.
 * @author Lahcen AHTAT
 */
@Composable
fun CommitCard(sha: String?, message: String?, author: String, date: String) {
    Card(
        {}, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .background(Color.Black)
    ) {
        if (sha == null || message == null) return@Card
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
                    text = message,
                    fontFamily = customFontFamily,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = "$author authored $date",
                    fontFamily = titleFont,
                    fontSize = 12.sp,
                    color = Orange,
                    modifier = Modifier.fillMaxWidth(0.6f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }

            Text(
                text = sha, fontFamily = customFontFamily, modifier = Modifier, color = Orange
            )
        }
    }
}