package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.ui.theme.topBarFont

@Composable
fun ProjectWorkItems(item: Item, openScreen: () -> Unit) {
    Card(
        openScreen,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 1.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(50.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(item.Id),
                contentDescription = item.name,
                Modifier.size(30.dp).padding(3.dp),
                tint = Orange
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.name,
                fontSize = 20.sp,
                fontFamily = topBarFont,
                modifier = Modifier.weight(0.9f)
            )
            Text(
                text = item.count.toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.End,
                fontFamily = topBarFont,
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}