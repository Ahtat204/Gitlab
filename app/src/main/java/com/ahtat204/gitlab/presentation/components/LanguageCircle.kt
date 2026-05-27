package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap


/**
 * Renders a circular color indicator representing a programming language
 * (such as Java, C#, Go, etc.) used in a GitLab repository.
 *
 * This composable draws a single circular point on a [Canvas] using
 * the provided color value. The point acts as a visual marker for the
 * language associated with the repository.
 *
 * @param color The integer color value (ARGB) used to represent the language.
 *
 * Example usage:
 * ```
 * Language(color = 0xFFB07219.toInt()) // Java orange color
 * Language(color = 0xFF00ADD8.toInt()) // Go blue color
 * ```
 *
 * The point is drawn with:
 * - A fixed stroke width of 26f to control its size.
 * - A [StrokeCap.Round] to ensure the point appears circular.
 */
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