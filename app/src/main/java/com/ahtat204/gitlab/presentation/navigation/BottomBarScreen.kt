package com.ahtat204.gitlab.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.twotone.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a screen in the bottom navigation bar of the app.
 *
 * Each object defines a route for navigation, a display title,
 * and an [ImageVector] icon for UI representation.
 *
 * @property route The navigation route associated with this screen.
 * @property title The title displayed in the bottom bar.
 * @property icon The icon displayed in the bottom bar.
 */
sealed class BottomBarScreen(
    val route: String, val title: String, val icon: ImageVector
) {
    companion object Routes {
        /** Route for the Home screen. */
        const val HOME = "home"
        /** Route for the Explore screen. */
        const val EXPLORE = "explore"
        /** Route for the Profile screen. */
        const val PROFILE = "profile"
        /** Route for the Activity screen. */
        const val ACTIVITY = "activity"
        /** Route for the Issues screen. */
        const val ISSUES = "issues"
        /** Route for the Personal projects screen. */
        const val PERSONAL = "personal"
        /** Route for the Branches/Commits screen with dynamic parameters. */
        const val BRANCHES = "commits/{projectId}/{branch}"
        /** Route for the Repository browser screen with project ID. */
        const val REPOSITORY = "repository?projectId={projectId}"
        /** Route for the Merge Requests screen. */
        const val MRS = "merge_requests"
        /** Route for the Project details screen with project ID. */
        const val PROJECT = "project?projectId={projectId}"
    }

    /** Represents the Home destination in the bottom bar. */
    data object Home : BottomBarScreen(HOME, "Home", Icons.Default.Home)
    /** Represents the Explore destination in the bottom bar. */
    data object Explore : BottomBarScreen(EXPLORE, "Explore", Icons.Filled.Explore)
    /** Represents the Profile destination in the bottom bar. */
    data object Profile : BottomBarScreen(PROFILE, "Profile", Icons.TwoTone.Person)
    /** Represents the Activity destination in the bottom bar. */
    data object Activity : BottomBarScreen(ACTIVITY, "Activity", Icons.Filled.AvTimer)
}