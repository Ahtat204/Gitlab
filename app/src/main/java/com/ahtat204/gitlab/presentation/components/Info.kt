package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/**
 * Displays a vertical list of informational rows, each consisting of an icon and a text label.
 *
 * ## Purpose
 * - Provides a reusable UI component for showing metadata or details with associated icons.
 * - Each entry is represented by a [Pair] of a string (text) and an [ImageVector] (icon).
 *
 * ## Parameters
 * @param infos A variable number of [Pair] values, where:
 * - `first`: The text string to display (nullable).
 * - `second`: The [ImageVector] icon associated with the text.
 *
 * ## Behavior
 * - Each non-empty string is rendered as a row with an icon and text.
 * - If the string is `null` or empty, the row is skipped.
 * - Icons are fixed at 17.dp size and aligned vertically with the text.
 * - Text is displayed with a font size of 14sp and padded for readability.
 *
 * ## Layout
 * - Rows are arranged vertically in a [Column].
 * - Each row uses a [Row] with horizontal alignment and padding.
 * - The entire column fills the available width and has top padding.
 *
 * ## Example
 * ```
Info(
Pair(profile.jobTitle ?: "", Icons.Default.Cases),
Pair(profile.location, Icons.Default.LocationOn)
)
 * ```
 *
 * ## Notes
 * - This composable is flexible and can be used for any list of labeled icons.
 * - The `contentDescription` of icons is set to `null` since they are decorative.
 * @author Lahcen AHTAT
 */
@Composable
fun Info(vararg infos: Pair<String?, ImageVector>) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(1.dp, 10.dp)
            .fillMaxWidth()
    ) {
        infos.forEach { inf ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(15.dp, 0.dp)
            ) {
                inf.first.let {
                    if (it == "") return
                    Icon(
                        imageVector = inf.second,
                        contentDescription = null,
                        modifier = Modifier
                            .size(17.dp)
                            .fillMaxSize()
                    )
                    Text(
                        text = it ?: "",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(10.dp, 10.dp),
                    )
                }
            }
        }
    }
}
