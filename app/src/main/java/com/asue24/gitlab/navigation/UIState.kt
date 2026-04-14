package com.asue24.gitlab.navigation
sealed class UiState {
   data object Loading : UiState()
    data object Authenticated : UiState()
    data object Unauthenticated : UiState()
}