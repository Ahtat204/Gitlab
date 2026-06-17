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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily

/**
 * for files
 */
@Composable
fun TreeItemCard(item: GetProjectRepositoryQuery.Node1?){
    item?.name?.let{
        Card(
            {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
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
                    Icons.Rounded.FileOpen,
                    contentDescription = item.id,
                    Modifier.size(30.dp).padding(3.dp),
                    tint = Orange
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = item.name,
                    fontFamily = customFontFamily,
                    modifier = Modifier.weight(0.9f)
                )
            }
        }
    }

}

/**
 * for folders
 */
@Composable
fun TreeItemCard(item: GetProjectRepositoryQuery.Node?){
    Card(
        {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 10.dp)
            .background(Color.Black)
    ) {
        item?.name?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Folder,
                    contentDescription = it,
                    Modifier.size(30.dp).padding(3.dp),
                    tint = Orange
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = it,
                    fontFamily = customFontFamily,
                    modifier = Modifier.weight(0.9f)
                )
            }
        }

    }
}