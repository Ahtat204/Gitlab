package com.asue24.gitlab.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asue24.gitlab.AuthenticationRepository
import com.asue24.gitlab.navigation.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val authRepo: AuthenticationRepository) : ViewModel() {

    // This is the state your NavHost will observe
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hasToken = authRepo.hasValidToken()

            if (hasToken) {
                _uiState.value = UiState.Authenticated
            } else {
                _uiState.value = UiState.Unauthenticated
            }
        }
    }
}