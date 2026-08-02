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
        const val HOME = "home"
        const val EXPLORE = "explore"
        const val PROFILE = "profile"
        const val ACTIVITY = "activity"
        const val ISSUES = "issues"
        const val PERSONAL = "personal"
        const val BRANCHES = "commits/{projectId}/{branch}"
        const val REPOSITORY = "repository?projectId={projectId}"
    }

    data object Home : BottomBarScreen(HOME, "Home", Icons.Default.Home)
    data object Explore : BottomBarScreen(EXPLORE, "Explore", Icons.Filled.Explore)
    data object Profile : BottomBarScreen(PROFILE, "Profile", Icons.TwoTone.Person)
    data object Activity : BottomBarScreen(ACTIVITY, "Activity", Icons.Filled.AvTimer)
}