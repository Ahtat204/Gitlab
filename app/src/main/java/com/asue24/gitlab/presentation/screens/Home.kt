package com.asue24.gitlab.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Home(navController: NavHostController, Login: () -> Unit) {
    Log.d("HomeScreen", "home screen is opened")
    Button(onClick = Login, modifier = Modifier.size(200.dp)) { Text(text = "HomeScreen") }
}