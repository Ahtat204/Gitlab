package com.asue24.gitlab.presentation.components
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.asue24.gitlab.presentation.navigation.BottomBarScreen


/**
 * Composable that displays the bottom navigation bar.
 *
 * @param navController Controller used to navigate between bottom bar screens.
 *
 * This bottom bar currently includes the Home, Calendar, and Dashboard screens.
 * Highlights the current selected screen based on the navigation state.
 */
@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomBarScreen.Home, BottomBarScreen.DashBoard, BottomBarScreen.Activity,BottomBarScreen.Profile
    )

    // Observe the current back stack entry to highlight the selected item
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(backgroundColor = Color.Black) {
        items.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

/**
 * Composable that adds a single item to the bottom navigation bar.
 *
 * @param screen The [BottomBarScreen] that this item represents.
 * @param currentDestination The current [NavDestination], used to determine selection state.
 * @param navController Controller used to navigate to the screen when clicked.
 *
 * Displays an icon and label for the item. Highlights the item if it is the
 * current destination in the navigation hierarchy.
 */
@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = { navController.navigate(route = screen.route) },
        label = { Text(text = screen.title) },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "${screen.title} icon"
            )
        }
    )
}