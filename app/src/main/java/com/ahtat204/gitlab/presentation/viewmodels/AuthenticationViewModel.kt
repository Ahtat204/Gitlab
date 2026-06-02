package com.ahtat204.gitlab.presentation.viewmodels

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.domain.usecase.authentication.authStateStore
import com.ahtat204.gitlab.domain.usecase.authentication.constants.AuthConfig
import com.ahtat204.gitlab.domain.usecase.authentication.constants.GlobalSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

@HiltViewModel
class AuthenticationViewModel : ViewModel() {
    private var authState: AuthState? = null
    private val authenticationService = AuthorizationService(GlobalSingleton.context)
    private var serviceConfig: AuthorizationServiceConfiguration? =
        AuthorizationServiceConfiguration(
            AuthConfig.AUTH_URI.toUri(), AuthConfig.TOKEN_URI.toUri()
        )
    private var authRequest: AuthorizationRequest? = AuthorizationRequest.Builder(
        serviceConfig!!,
        AuthConfig.CLIENT_ID,
        ResponseTypeValues.CODE,
        AuthConfig.CALLBACK_URL.toUri()
    ).setScope(AuthConfig.SCOPE).build()

    fun authententicate(launcher: ActivityResultLauncher<Intent>) {
        val authIntent = authenticationService.getAuthorizationRequestIntent(authRequest!!)
            ?: throw NullPointerException("Intent is null")
        launcher.launch(authIntent)
        authState = AuthState(serviceConfig!!)
    }

    fun exchangeCodeForToken(
        service: AuthorizationService, response: AuthorizationResponse, authState: AuthState
    ) {
        authState.update(response, null)
        val tokenRequest = response.createTokenExchangeRequest()
        service.performTokenRequest(tokenRequest) { tokenResponse, ex ->
            if (tokenResponse != null) {
                authState.update(tokenResponse, ex)
                viewModelScope.launch {
                    GlobalSingleton.context.authStateStore.updateData { authState }
                }
                GlobalSingleton.accessToken = authState.accessToken
                GlobalSingleton.CurrentAuthState = authState
            }
        }
    }
}