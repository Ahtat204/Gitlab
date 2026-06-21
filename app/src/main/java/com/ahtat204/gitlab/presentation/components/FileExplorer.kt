package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class FolderTree(var previous:String?,var current: String?)

/**
 *
 */
@Composable
fun FileExplorer(paths: List<String>) {
    paths.forEach { path ->
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(path)
        }
    }
}