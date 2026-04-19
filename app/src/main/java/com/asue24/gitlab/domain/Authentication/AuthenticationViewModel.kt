package com.asue24.gitlab.domain.Authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asue24.gitlab.data.repositories.AuthenticationRepository
import com.asue24.gitlab.domain.Authentication.constants.AuthStorage
import com.asue24.gitlab.presentation.navigation.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val authRepo: AuthenticationRepository) : ViewModel() {

    // This is the state your NavHost will observe
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hasToken = AuthStorage.getAuthState(this@AuthenticationViewModel.authRepo.cntx).data.first().refreshToken != null

            if (hasToken) {
                _uiState.value = UiState.Authenticated
            } else {
                _uiState.value = UiState.Unauthenticated
            }
        }
    }
}