package com.asue24.gitlab.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Timer
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
    /** Home screen, represented by a home icon. */
    data object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    /** Dashboard screen, represented by a star icon. */
    data object Projects : BottomBarScreen(
        "projects", "Projects", Icons.Filled.Folder
    )
    data object Profile : BottomBarScreen("profile", "Profile", Icons.TwoTone.Person)
    data object Activity : BottomBarScreen("activity", "Activity", Icons.Filled.AvTimer)
}