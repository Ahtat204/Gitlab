package com.ahtat204.gitlab.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.ahtat204.gitlab.R
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context
import com.ahtat204.gitlab.presentation.components.ContactLinks
import com.ahtat204.gitlab.presentation.components.Header
import com.ahtat204.gitlab.presentation.ui.theme.Orange
import com.ahtat204.gitlab.presentation.ui.theme.customFontFamily
import com.ahtat204.gitlab.presentation.ui.theme.titleFont
import com.ahtat204.gitlab.presentation.viewmodels.ProfileViewModel
import kotlinx.coroutines.Dispatchers

/**
 * A profile screen composable responsible for orchestrating the user's account information display.
 *
 * This screen follows the MVVM architecture, utilizing [ProfileViewModel] to fetch and manage
 * user profile state asynchronously. Upon initialization, it triggers a profile load operation
 * and reacts to state changes via [collectAsState].
 *
 * @param navController The [NavHostController] used for handling inter-screen navigation.
 * @param x The [PaddingValues] injected by the parent scaffold/layout, ensuring proper
 * alignment relative to system UI elements (e.g., bottom bars or status bars).
 * @param profileViewModel The Hilt-injected ViewModel responsible for business logic and
 * data persistence for the profile state.
 *
 * @note This component instantiates a local [ImageLoader] with specific IO dispatchers and
 * cache configurations, optimized for profile imagery.
 * *
 *
 * @see ProfileViewModel
 * @see Header
 */
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
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Your Profile", fontFamily = titleFont, fontSize = 20.sp, color = Orange)
            profile.avatarUrl?.let { Log.d("url", it) }
            Header(
                profile.name,
                profile.username,
                profile.status?.message ?: "",
                loader,
                profile.avatarUrl
            )
            //="https://github.com/${profile.github?:""}"
            val github=if(profile.github !=null)Pair("https://github.com/${profile.github?:""}",R.drawable.github) else Pair(null,null)
            Text(
                profile.jobTitle ?: "",
                fontFamily = titleFont,
                fontSize = 20.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(2.dp, 5.dp),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = profile.bio ?: "",
                overflow = TextOverflow.Ellipsis,
                maxLines = 10,
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(0.9f),
                fontFamily = customFontFamily,
            )

            val linked:Pair<String?,Int?> =if(profile.linkedin==null) Pair(null,null) else Pair(profile.linkedin,R.drawable.linkedin)
            ContactLinks(linked,github)

        }
    }

}