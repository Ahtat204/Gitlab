package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily

@Composable
fun CommitCard(sha: String?, message: String?) {
    Card(
        {}, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .background(Color.Black)
    ) {
        if (sha == null || message == null) return@Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(50.dp)
        ) {
            Text(
                text = message, fontFamily = customFontFamily, modifier = Modifier
            )
            Text(
                text = sha, fontFamily = customFontFamily, modifier = Modifier
            )
        }
    }
}