package com.ahtat204.gitlab.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ahtat204.gitlab.presentation.navigation.BottomBarScreen
import com.ahtat204.gitlab.presentation.activities.ui.theme.Orange
import com.ahtat204.gitlab.presentation.activities.ui.theme.titleFont

@Composable
fun TopAppBar(name: String?, navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        navController.currentDestination?.route?.let {
            if (it == BottomBarScreen.Home.route || it == BottomBarScreen.Profile.route || it == BottomBarScreen.Activity.route || it == BottomBarScreen.Explore.route) return
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Rounded.ArrowBack, contentDescription = null
                )
            }
        }
        Text(
            text = "Your $name",
            fontFamily = titleFont,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Orange,
            modifier = Modifier.fillMaxWidth()
        )
    }

}