package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery.Tree

@Composable
fun FileExplorer(tree: Tree?) {
    Column(
        modifier = Modifier.border(
            width = (0.1f).dp, color = Color(0xFF675353), shape = RoundedCornerShape(10.dp)
        )
            .padding(0.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tree?.trees?.nodes?.let { folders ->
            folders.forEach {
                TreeItemCard(it)
            }
        }
        tree?.blobs?.nodes?.let { files ->
            files.forEach {
                TreeItemCard(it)
            }
        }
    }

}