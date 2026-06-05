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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import java.util.regex.Pattern

@Composable
fun AutoLinkText(
    text: String,
    modifier: Modifier = Modifier,
    fontFamily: FontFamily = FontFamily.Default
) {
    val urlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+[/#?]?.*)".toRegex()
    val annotatedString = buildAnnotatedString {
        var lastIndex = 0
        urlRegex.findAll(text).forEach { match ->
            append(text.substring(lastIndex, match.range.first))
            withLink(
                LinkAnnotation.Url(
                    url = match.value,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            fontFamily = fontFamily,
                            fontSize = 14.sp,
                            color = Orange,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
            ) {
                append(match.value)
            }

            lastIndex = match.range.last + 1
        }
        // Append remaining text
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