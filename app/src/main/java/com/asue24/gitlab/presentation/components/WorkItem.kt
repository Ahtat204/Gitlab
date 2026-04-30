package com.asue24.gitlab.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asue24.gitlab.R

/**
 *
 */
@Composable
fun WorkItem(name: String, openScreen: () -> Unit) {
    Card(openScreen, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Icon(
                painter = painterResource(R.drawable.mergerequest),
                contentDescription = name
                , Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = name)
        }
    }
}