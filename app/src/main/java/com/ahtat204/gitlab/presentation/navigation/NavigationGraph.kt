package com.ahtat204.gitlab.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ahtat204.gitlab.presentation.screens.Home
import com.ahtat204.gitlab.presentation.screens.PersonalProjects
import com.ahtat204.gitlab.presentation.screens.Profile
import com.ahtat204.gitlab.presentation.screens.ProjectMergeRequests
import com.ahtat204.gitlab.presentation.screens.project.ProjectDetailScreen
import com.ahtat204.gitlab.presentation.screens.project.RepositoryScreen

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
//        composable(route = BottomBarScreen.Projects.route) {}
        composable(route = BottomBarScreen.Profile.route) {
            Profile(navController,x)
        }
        composable(route = "personal") {
            PersonalProjects(navController, x)
        }
        composable(route = BottomBarScreen.Activity.route) {
            // Activity screen placeholder
        }
        composable(route = "commits/{projectId}/{branch}",
            arguments = listOf(
                navArgument("projectId") { type = NavType.StringType },
                navArgument("branch") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        )
        { backStackEntry ->

            val projectId = backStackEntry.arguments?.getString("projectId")
            val branch=backStackEntry.arguments?.getString("branch")
            if(branch!=null && projectId!=null) {
               // ProjectCommits(navController, x, branch,projectId)
            }
        }

        navigation(startDestination ="Project", route = "project" ){
            composable(route = "repository?projectId={projectId}",
                arguments = listOf(navArgument("projectId") { defaultValue = "" })
            )
            {backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                projectId?.let { RepositoryScreen(it,x,navController) }
            }
            composable(
                route = "project?projectId={projectId}",
                arguments = listOf(navArgument("projectId") { defaultValue = "" })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                projectId?.let { ProjectDetailScreen(navController,x, it)
                }
            }
            composable(route = "mergerequests?projectId={projectId}",arguments = listOf(navArgument("projectId") { defaultValue = "" })) {backStackEntry ->
                   val projectId=backStackEntry.arguments?.getString("projectId")
                projectId?.let{ ProjectMergeRequests(it,navController,x) }
            }
        }
    }
}
