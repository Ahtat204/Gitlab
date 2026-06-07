package com.ahtat204.gitlab.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahtat204.gitlab.presentation.components.CommitCard
import com.ahtat204.gitlab.presentation.screens.Home
import com.ahtat204.gitlab.presentation.screens.PersonalProjects
import com.ahtat204.gitlab.presentation.screens.ProjectCommits
import com.ahtat204.gitlab.presentation.screens.ProjectDetailScreen

/**
 * Defines the navigation graph for the bottom navigation bar.
 *
 * This composable sets up the [NavHost] with routes corresponding to
 * different screens in the application. It manages navigation between:
 * - [Home] screen
 * - [PersonalProjects] screen
 * - [ProjectDetailScreen] with a dynamic project ID
 * - Placeholder routes for Profile and Activity
 *
 * @param navController The [NavHostController] used to handle navigation actions.
 * @param x The [PaddingValues] applied to the content area, typically provided
 * by the [androidx.compose.material.Scaffold] in the parent activity.
 *
 * Example usage:
 * ```
 * Scaffold(
 *     bottomBar = { BottomBar(navController) }
 * ) { paddingValues ->
 *     BottomNavigationGraph(navController, paddingValues)
 * }
 * ```
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavigationGraph(
    navController: NavHostController,
    x: PaddingValues
) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.Home.route) {
            Home(navController, x)
        }
        composable(route = BottomBarScreen.Profile.route) {
            // Profile screen placeholder
        }
        composable(route = "personal") {
            PersonalProjects(navController, x)
        }
        composable(route = BottomBarScreen.Activity.route) {
            // Activity screen placeholder
        }
        composable(route = "commits?projectId={projectsId}",arguments = listOf(navArgument("projectId") { defaultValue = "" })) {backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            projectId?.let { ProjectCommits(navController,x,it) }
        }
        composable(
            route = "project?projectId={projectId}",
            arguments = listOf(navArgument("projectId") { defaultValue = "" })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            projectId?.let { ProjectDetailScreen(navController,x, it) }
        }
    }
}
