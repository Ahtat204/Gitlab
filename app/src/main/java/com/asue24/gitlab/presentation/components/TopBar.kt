package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * the name represent the name of the screen ,as this Bar will contain useful buttons (like search and refresh )
 */
@Composable
fun TopBar(name: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            modifier = Modifier
                .weight(0.6f)
                .padding(20.dp, 0.dp),
            color = Color.White,
            fontSize = 27.sp
        )
        IconButton(onClick = {}, modifier = Modifier.weight(0.5f)) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier
            )
        }
        IconButton(onClick = {}, modifier = Modifier.weight(0.5f)) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier
            )
        }
    }
}