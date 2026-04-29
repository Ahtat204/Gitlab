package com.asue24.gitlab.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asue24.gitlab.presentation.components.ProjectDetailScreen

@Composable
fun BottomNavigationgraph(
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
        ProjectDetailScreen("Ahtat204/e-store-orderservice","")
        }
        composable(route = BottomBarScreen.Projects.route) {
        //LoginScreen(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {

        }
        composable(route = BottomBarScreen.Activity.route) {}
    }
}