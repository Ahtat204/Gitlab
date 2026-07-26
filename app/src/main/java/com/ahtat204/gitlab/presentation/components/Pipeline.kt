package com.ahtat204.gitlab.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.presentation.ui.theme.Background
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Pipeline(name: String?, statusEnum: PipelineStatusEnum, duration: Int?, finishedAt: Any?) {
    Card(
        {}, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Background)
            ) {
                PipeLineStatusIcon(statusEnum)
                Text(
                    text = iso8601ToRelative(finishedAt as String),
                    fontFamily = customFontFamily,
                )
                Text(
                    text = duration.toString(),
                    fontFamily = customFontFamily,
                )
            }
        }
    }
}