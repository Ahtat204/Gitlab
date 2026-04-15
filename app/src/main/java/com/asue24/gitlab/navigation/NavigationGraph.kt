package com.asue24.gitlab.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asue24.gitlab.AuthenticationRepository
import com.asue24.gitlab.MainActivity
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.screens.DashBoard
import com.asue24.gitlab.screens.Home
import com.asue24.gitlab.screens.LoginScreen
import com.asue24.gitlab.utility.refreshAccessToken
import com.asue24.gitlab.viewmodels.AuthenticationViewModel
import kotlinx.coroutines.flow.first
import net.openid.appauth.AuthState



@Composable
fun BottomNavigationgraph(
    navController: NavHostController, context: Context, AuthRepository: AuthenticationRepository
) {
    val refTokne = AuthStorage.getInstance(context).data.toString()
    val startDes =
        if (refTokne.isEmpty()) BottomBarScreen.Login.route else BottomBarScreen.Home.route

    NavHost(navController = navController, startDestination = startDes) {
        composable(route = BottomBarScreen.Home.route) {
         //   Home(navController)
        }
        composable(route = BottomBarScreen.Login.route) {
        //LoginScreen(navController)
        }
        composable(route = BottomBarScreen.DashBoard.route) {
            DashBoard()
        }
    }
}
@Composable
fun AppNavGraph(navController: NavHostController, viewModel: AuthenticationViewModel,activity: MainActivity,AuthRepository: AuthenticationRepository) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            LaunchedEffect(uiState) {
                when (uiState) {
                    is UiState.Authenticated -> {
                        refreshAccessToken(AuthStorage.getAuthState(activity).data.first(),AuthRepository.authService,AuthStorage.getInstance(activity).data.first().refreshToken!!,activity)
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
        composable(route = BottomBarScreen.Home.route) { Home(navController,Login = {
               // Here you put your existing click logic
            activity?.let { act ->
                val authIntent = act.getService().getAuthorizationRequestIntent(act.authRequest!!)
                    ?: throw NullPointerException("Intent is null")
                act.launcher.launch(authIntent)
                act.authState = AuthState(act.serviceConfig!!)
            }
        }) }
        composable(BottomBarScreen.Login.route) {
    LoginScreen(
        Login = {
            // Here you put your existing click logic
            activity?.let { act ->
                val authIntent = act.getService().getAuthorizationRequestIntent(act.authRequest!!)
                    ?: throw NullPointerException("Intent is null")
                act.launcher.launch(authIntent)
                act.authState = AuthState(act.serviceConfig!!)
            }
        }
    )
}
    }
}
