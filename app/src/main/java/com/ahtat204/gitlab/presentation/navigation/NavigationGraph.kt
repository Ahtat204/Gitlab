package com.ahtat204.gitlab.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahtat204.gitlab.presentation.screens.Home
import com.ahtat204.gitlab.presentation.screens.PersonalProjects
import com.ahtat204.gitlab.presentation.screens.ProjectDetailScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigationGraph(
    navController: NavHostController, x: PaddingValues
) {
    val path= remember{ mutableStateOf("") }
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            Home(navController, x)
        }
//        composable(route = BottomBarScreen.Projects.route) {}
        composable(route = BottomBarScreen.Profile.route) {}
        composable(route = "personal") {
            PersonalProjects(navController,x)
            navController.navigate(ProjectDetailScreen(x,path.value))
        }
        composable(route = BottomBarScreen.Activity.route) {}
        composable(route ="project"){
            ProjectDetailScreen(x,path.value)
        }
    }
}