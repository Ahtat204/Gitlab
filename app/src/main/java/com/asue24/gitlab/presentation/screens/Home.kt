package com.asue24.gitlab.presentation.screens

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
import com.asue24.gitlab.presentation.components.MyWorkItems
import com.asue24.gitlab.presentation.components.TopBar

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