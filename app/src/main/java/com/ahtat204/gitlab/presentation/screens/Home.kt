package com.ahtat204.gitlab.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.components.MyWorkItems
/**
 * Composable that represents the Home screen of the application.
 *
 * ## Overview
 * - Provides a container layout for the "Your Work" section.
 * - Wraps [MyWorkItems] inside a [Column] with padding and background styling.
 * - Serves as the entry point for navigating to different work categories.
 *
 * ## Parameters
 * @param navController The [NavHostController] used to handle navigation between screens.
 * @param x The [PaddingValues] applied to the column for proper spacing.
 *
 * ## UI Behavior
 * - Fills the available height and applies a black background.
 * - Aligns content at the top and centers horizontally.
 * - Delegates rendering of work items to [MyWorkItems].
 *
 * ## Example
 * ```kotlin
 * Home(
 *     navController = rememberNavController(),
 *     x = PaddingValues(16.dp)
 * )
 * ```
 *
 * ## Notes
 * - This composable acts as a wrapper for [MyWorkItems], ensuring consistent
 *   layout and styling across the home screen.
 *   @see <img src="https://raw.githubusercontent.com/Ahtat204/Gitlab/refs/heads/screen/project/repository/homescreen.jpg"  width="300" height="700"/>
 */
@Composable
fun Home(navController: NavHostController,x: PaddingValues) {
    Column(
        modifier = Modifier.padding(x)
            .fillMaxHeight()
            .background(Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MyWorkItems(navController)
    }
}