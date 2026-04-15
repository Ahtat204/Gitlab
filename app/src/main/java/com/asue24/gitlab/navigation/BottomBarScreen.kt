package com.asue24.gitlab.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
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
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Login : BottomBarScreen("login", "Login", Icons.Default.Email)
    /** Home screen, represented by a home icon. */
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)


    /** Dashboard screen, represented by a star icon. */
    object DashBoard : BottomBarScreen("dashboard", "Dashboard", Icons.Rounded.Star)

    /** Screen for creating a task, represented by a plus/add icon. */
    object CreateTask : BottomBarScreen("create", "Create", Icons.Rounded.Add)
}