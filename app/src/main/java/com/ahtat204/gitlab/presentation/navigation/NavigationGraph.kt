package com.ahtat204.gitlab.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahtat204.gitlab.presentation.screens.Home
import com.ahtat204.gitlab.presentation.screens.PersonalProjects
import com.ahtat204.gitlab.presentation.screens.Profile

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigationGraph(
    navController: NavHostController, x: PaddingValues
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            Home(navController, x)
        }
//        composable(route = BottomBarScreen.Projects.route) {}
        composable(route = BottomBarScreen.Profile.route) {
            Profile(navController,x)
        }
        composable(route = "personal") {
            PersonalProjects(x)
        }
        composable(route = BottomBarScreen.Activity.route) {}
    }
}