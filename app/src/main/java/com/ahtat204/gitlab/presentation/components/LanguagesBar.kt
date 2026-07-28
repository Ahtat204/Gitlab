package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LanguagesBar() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        // Hard edges created by repeating each color at its stop
        val brush = Brush.linearGradient(
            0.00f to Color.Red,      // 0%   - Red
            0.33f to Color.Red,      // 33%  - hard edge
            0.33f to Color.Green,    // start Green
            0.66f to Color.Green,    // end Green
            0.66f to Color.Blue,     // start Blue
            1.00f to Color.Blue
        )

        drawLine(
            brush = brush,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 11f
        )
    }
}