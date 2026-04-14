package com.asue24.gitlab.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import net.openid.appauth.AuthState

@Composable
fun LoginScreen(Login:()->Unit) {
    Button(onClick = Login
        ,modifier = Modifier.size(200.dp)) { Text(text = "Signup") }

}