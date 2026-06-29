package com.ahtat204.gitlab.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DraggablePanelExample() {
    val scope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }
    val offsetY = remember { Animatable(1000f) } // Start off-screen

    Box(modifier = Modifier.fillMaxSize()) {
        // Button to show panel
        Button(
            onClick = {
                isVisible = true
                scope.launch { offsetY.animateTo(0f, tween(300)) }
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Show Draggable Panel")
        }

        if (isVisible) {
            // Dimmed background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable {
                        scope.launch {
                            offsetY.animateTo(1000f, tween(300))
                            isVisible = false
                        }
                    }
            )

            // Draggable panel
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = offsetY.value.dp)
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val newOffset = (offsetY.value + dragAmount.y).coerceAtLeast(0f)
                                scope.launch { offsetY.snapTo(newOffset) }
                            },
                            onDragEnd = {
                                scope.launch {
                                    if (offsetY.value > 150f) {
                                        // Close if dragged down enough
                                        offsetY.animateTo(1000f, tween(300))
                                        isVisible = false
                                    } else {
                                        // Snap back up
                                        offsetY.animateTo(0f, tween(200))
                                    }
                                }
                            }
                        )
                    }
            ) {
                Text(
                    "Drag me down to close",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
            }
        }
    }
}