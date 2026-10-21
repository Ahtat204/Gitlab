package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.activities.ui.theme.customFontFamily


@Composable
fun <T> Tag(icon: T, content: String) {
    val size = content.length + 50
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFF443E3E))
            .height(15.dp)
            .width(size.dp)
    ) {

        when (icon) {
            is Int -> {
                Icon(
                    painter = painterResource(icon), contentDescription = null, Modifier
                        .size(10.dp)
                        .offset(x = 7.dp)
                )
            }

            is ImageVector -> {
                Icon(
                    imageVector = icon, contentDescription = null, Modifier
                        .size(10.dp)
                        .offset(x = 7.dp)
                )
            }

            else -> Unit
        }
        Text(
            text = content,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .offset(10.dp, 0.dp),
            fontFamily = customFontFamily,
        )
    }

}