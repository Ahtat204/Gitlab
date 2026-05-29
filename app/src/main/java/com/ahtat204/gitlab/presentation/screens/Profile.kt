package com.ahtat204.gitlab.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context
import com.ahtat204.gitlab.presentation.components.Header
import com.ahtat204.gitlab.presentation.viewmodels.ProfileViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun Profile(
    navController: NavHostController,
    x: PaddingValues,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }
    val user by profileViewModel.currentUser.collectAsState()
    val loader: ImageLoader =
        ImageLoader.Builder(context).crossfade(true).dispatcher(Dispatchers.IO)
            .respectCacheHeaders(false).build()
    user?.let { profile ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(x)
                .background(Color.Black).fillMaxSize()
        ) {
            profile.avatarUrl?.let { Log.d("url", it) }
            Header(
                profile.name, profile.username, profile.status?.message?:"", loader, profile.avatarUrl
            )
        }
    }

}