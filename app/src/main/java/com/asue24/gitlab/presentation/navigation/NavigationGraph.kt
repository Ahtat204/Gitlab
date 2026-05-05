package com.asue24.gitlab.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asue24.gitlab.presentation.screens.Home

@Composable
fun BottomNavigationgraph(
    navController: NavHostController,x: PaddingValues
) {

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
        Home(navController,x)
        }
        composable(route = BottomBarScreen.Projects.route) {
        //LoginScreen(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {

        }
        composable(route = BottomBarScreen.Activity.route) {}
    }
}