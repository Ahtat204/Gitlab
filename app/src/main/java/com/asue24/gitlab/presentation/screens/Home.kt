package com.asue24.gitlab.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.asue24.gitlab.presentation.components.MyWorkItems
import com.asue24.gitlab.presentation.components.TopBar

@Composable
fun Home(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally){
        TopBar("Home")
        MyWorkItems(navController)
    }
}