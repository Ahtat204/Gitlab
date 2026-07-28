package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
/**
 * Renders text with automatically detected hyperlinks.
 *
 * ## Purpose
 * - Scans the input [text] for URLs using a regex pattern.
 * - Converts detected URLs into clickable links with styled annotations.
 * - Displays the full text with links highlighted and underlined.
 *
 * ## Parameters
 * @param text The input string to render. Any substring matching the URL regex
 *             will be converted into a clickable link.
 * @param fontFamily The font family applied to link styling. Defaults to [FontFamily.Default].
 *
 * ## Behavior
 * - Uses a regex to detect URLs starting with `http://` or `https://`.
 * - Non-URL text is appended normally.
 * - Each detected URL is wrapped in a [LinkAnnotation.Url] with custom [TextLinkStyles].
 * - Links are styled with:
 *   - Orange color
 *   - Underline decoration
 *   - Font size of 14sp
 *   - Provided [fontFamily]
 * - The entire text block is rendered with:
 *   - White color
 *   - Font size of 15sp
 *   - [titleFont] family
 *   - Padding of (20.dp, 10.dp)
 *   - Left alignment
 *
 * ## Example
 * ```
 * AutoLinkText(
 *     text = "Check out https://gitlab.com for more info",
 *     fontFamily = FontFamily.Serif
 * )
 * ```
 *
 * ## Notes
 * - Each URL is prepended with a newline (`\n`) before being appended.
 * - The `contentDescription` for links is omitted since they are textual.
 * - Extend the regex if you want to support additional link formats (e.g., mailto, ftp).
 * @author Lahcen AHTAT
 */
@Composable
fun AutoLinkText(
    text: String, fontFamily: FontFamily = FontFamily.Default
) {
    val urlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+[/#?]?.*)".toRegex()
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        urlRegex.findAll(text).forEach { match ->
            append(text.substring(lastIndex, match.range.first))
            withLink(
                LinkAnnotation.Url(
                    url = match.value, styles = TextLinkStyles(
                        style = SpanStyle(
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            color = Orange,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
            ) {
                append("\n" + match.value)
            }
            lastIndex = match.range.last + 1
        }
        append(text.substring(lastIndex))
    }

    Text(
        annotatedString,
        fontFamily = titleFont,
        fontSize = 15.sp,
        modifier = Modifier.padding(20.dp, 10.dp),
        color = Color.White,
        textAlign = TextAlign.Start
    )
}