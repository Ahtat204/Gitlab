package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RepositoryHead(commitMessage: String,timeline:String,rootRef:String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(50.dp),
            verticalArrangement =  Arrangement.Center
            , horizontalAlignment =Alignment.CenterHorizontally
        ) {
            Text(commitMessage)
            Text(timeline)
        }
        TextButton(onClick = {}) {
            Text(rootRef)
        }
    }
}