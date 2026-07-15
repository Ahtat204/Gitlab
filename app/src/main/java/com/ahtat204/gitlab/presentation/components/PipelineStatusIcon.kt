package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
/**
 * Displays a status icon representing the current state of a GitLab pipeline.
 *
 * ## Purpose
 * - Provides a visual indicator for pipeline status (e.g., success, failed).
 * - Uses different icons and background colors depending on the [PipelineStatusEnum].
 * - Intended to be used within project or pipeline detail screens.
 *
 * ## Parameters
 * @param status The current pipeline status, represented by [PipelineStatusEnum].
 *
 * ## Behavior
 * - [PipelineStatusEnum.SUCCESS]: Shows a success icon with a green background.
 * - [PipelineStatusEnum.FAILED]: Shows a failed icon with a red background.
 * - Other statuses (Created, Pending, Canceled, Running): Currently render no icon.
 *
 * ## Layout
 * - Icon is offset by (310.dp, 40.dp) from its parent.
 * - Icon size is fixed at 20.dp.
 * - Icon is clipped to a rounded rectangle shape with 10.dp corner radius.
 * - Background color is applied behind the icon for visual distinction.
 *
 * ## Example
 * ```
 * PipeLineStatusIcon(status = PipelineStatusEnum.SUCCESS)
 * ```
 *
 * ## Notes
 * - Extend this composable to handle additional statuses (Pending, Running, etc.)
 *   by adding corresponding icons and colors.
 * - The `contentDescription` is set to `null` since icons are purely decorative.
 * @author Lahcen AHTAT
 */
@Composable
fun PipeLineStatusIcon(status: PipelineStatusEnum) {
    when (status) {
        PipelineStatusEnum.CREATED -> {Unit}
        PipelineStatusEnum.SUCCESS -> {
            Icon(
                painter = painterResource(R.drawable.status_success),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .offset(310.dp, 40.dp)
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp)) // Clip first
                    .background(Color(0xFF30671B))                      // Optional inner padding
            )
        }
        PipelineStatusEnum.PENDING->{}
        PipelineStatusEnum.CANCELED->{}
        PipelineStatusEnum.RUNNING->{}
        PipelineStatusEnum.FAILED -> {
            Icon(
                painter = painterResource(R.drawable.failed),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .offset(310.dp, 40.dp)
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp)) // Clip first
                    .background(Color(0xFFAF2A2A))                      // Optional inner padding
            )
        }
        PipelineStatusEnum.UNKNOWN__ -> {

        }
        else -> {

        }
    }

}