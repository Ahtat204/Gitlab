package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetExample() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    // Button to open sheet
    Button(onClick = { showSheet = true }) {
        Text("Show Draggable Panel")
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            // Sheet content
            Text(
                "Drag me down to close",
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    scope.launch { sheetState.hide() }
                    showSheet = false
                },
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            ) {
                Text("Close")
            }
        }
    }
}