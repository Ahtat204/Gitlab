package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
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

@Composable
fun ContactLinks(vararg links: Pair<String?, Int?>) {
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