package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap



@Composable
fun Language(color: Int) {

    Canvas(
        modifier = Modifier
    ) {
        val center = Offset(1.0f, size.height / 2)
        drawPoints(
            points = listOf(center),
            pointMode = PointMode.Points,
            color = Color(color),
            strokeWidth = 26f, // Controls point size
            cap = StrokeCap.Round // Makes the point circular
        )
    }}