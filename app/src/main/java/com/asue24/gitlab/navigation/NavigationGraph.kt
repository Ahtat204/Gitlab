package com.asue24.gitlab.navigation

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asue24.gitlab.AuthenticationRepository
import com.asue24.gitlab.MainActivity
import com.asue24.gitlab.constants.AuthStorage
import com.asue24.gitlab.screens.DashBoard
import com.asue24.gitlab.screens.Home



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

tailrec fun Context.getActivity(): MainActivity? = when (this) {
    is MainActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
