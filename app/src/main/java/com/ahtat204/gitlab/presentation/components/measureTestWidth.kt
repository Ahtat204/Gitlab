package com.ahtat204.gitlab.presentation.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

@Composable
fun measureTextWidth(text: String, style: TextStyle = LocalTextStyle.current): Dp {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text = text, style = style).size.width

    return with(LocalDensity.current) { widthInPixels.toDp() }
}