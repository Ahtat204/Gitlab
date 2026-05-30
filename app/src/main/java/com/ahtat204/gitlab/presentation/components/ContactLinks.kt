package com.ahtat204.gitlab.presentation.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context
import com.ahtat204.gitlab.presentation.ui.theme.Orange

@Composable
fun ContactLinks(vararg links: String) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(1.dp, 10.dp)
            .fillMaxWidth()
    ) {
        links.forEach { link ->
            val annotatedText = buildAnnotatedString {
                append("Check out ")
                pushStringAnnotation(
                    tag = "URL", annotation = link
                )
                withStyle(
                    style = SpanStyle(
                        color = Orange, fontSize = 16.sp, textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(link)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Link,
                    contentDescription = "link",
                    Modifier
                        .size(13.dp),
                    tint = Orange
                )
                ClickableText(
                    text = annotatedText, onClick = { offset ->
                        annotatedText.getStringAnnotations("URL", offset, offset).firstOrNull()
                            ?.let { annotation ->
                                val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            }
                    })
            }
        }
    }

}