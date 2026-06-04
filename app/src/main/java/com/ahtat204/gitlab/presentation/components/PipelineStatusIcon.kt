package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum

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

        else -> {}
    }

}