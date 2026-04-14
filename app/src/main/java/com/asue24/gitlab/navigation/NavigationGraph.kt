package com.asue24.gitlab.navigation

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asue24.gitlab.AuthenticationRepository
import com.asue24.viewmodels.AuthenticationViewModel
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.screens.DashBoard
import com.asue24.gitlab.screens.Home
import com.asue24.gitlab.screens.LoginScreen

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: AuthenticationViewModel,Login:()->Unit) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            // Observe the state and navigate accordingly
            LaunchedEffect(uiState) {
                when (uiState) {
                    is UiState.Authenticated -> {
                        navController.navigate("home") { popUpTo(0) }
                    }
                    is UiState.Unauthenticated -> {
                        navController.navigate("login") { popUpTo(0) }
                    }
                    else -> {
                    }
                }
            }
        }
        composable(route = BottomBarScreen.Home.route) { Home(navController) }
        composable(route = BottomBarScreen.Login.route) { LoginScreen(navController,Login) }
    }
}

@Composable
fun BottomNavigationgraph(
    navController: NavHostController, context: Context, AuthRepository: AuthenticationRepository
) {
    val refTokne = AuthStorage.getInstance(context).data.toString()
    val startDes =
        if (refTokne.isEmpty()) BottomBarScreen.Login.route else BottomBarScreen.Home.route

    NavHost(navController = navController, startDestination = startDes) {
        composable(route = BottomBarScreen.Home.route) {
            Home(navController)
        }
        composable(route = BottomBarScreen.Login.route) {
       //     LoginScreen(navController)
        }
        composable(route = BottomBarScreen.DashBoard.route) {
            DashBoard()
        }
    }
}

@Composable
fun HomeScreen() {
    Text(text = "Login", fontSize = 50.sp, color = Color.White)
}
