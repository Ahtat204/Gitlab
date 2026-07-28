package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.ui.theme.topBarFont
/**
 * Displays a vertical list of contact links, each with an optional icon and clickable text.
 *
 * ## Purpose
 * - Provides a reusable UI component for showing external links (e.g., GitHub, LinkedIn, personal site).
 * - Each entry is represented by a [Pair] of a URL string and an optional drawable resource ID.
 *
 * ## Parameters
 * @param links A variable number of [Pair] values, where:
 * - `first`: The URL string to display as clickable text (nullable).
 * - `second`: The drawable resource ID for the icon associated with the link (nullable).
 *
 * ## Behavior
 * - If `first` is non-null, it is rendered as a clickable link using [LinkAnnotation.Url].
 * - Links are styled with:
 *   - White color
 *   - Underline decoration
 *   - Font size of 14sp
 *   - [topBarFont] family
 * - If `second` is non-null, an [Icon] is displayed before the link text.
 * - Each link is displayed in a [Row] with padding and vertical alignment.
 *
 * ## Layout
 * - Root: [Column] with top alignment and full width.
 * - Each entry: [Row] containing:
 *   - Optional [Icon] (17.dp size).
 *   - [Text] with clickable link annotation and padding.
 *
 * ## Example
 * ```
 * Contact(
 *     "https://github.com/ahtat204" to R.drawable.github,
 *     "https://linkedin.com/in/ahtat204" to R.drawable.linkedin
 * )
 * ```
 *
 * ## Notes
 * - The `contentDescription` for icons is set to `null` since they are decorative.
 * - Extend this composable to support non-URL text (e.g., email addresses) by adjusting the regex or annotation logic.
 * @author Lahcen AHTAT
 */
@Composable
fun Contact(vararg links: Pair<String?, Int?>) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(1.dp, 10.dp)
            .fillMaxWidth()
    ) {
        links.forEach { link ->
            val annotatedText = buildAnnotatedString {
                link.first?.let {
                    withLink(
                        LinkAnnotation.Url(
                            url = it, styles = TextLinkStyles(
                                style = SpanStyle(
                                    fontFamily = topBarFont,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        )
                    ) {
                        append(link.first)
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(15.dp, 0.dp)
            ) {
                link.second?.let {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(17.dp)
                            .fillMaxSize()
                    )
                }
                Text(
                    annotatedText,
                    modifier = Modifier.padding(10.dp, 10.dp),
                )
            }
        }
    }

}