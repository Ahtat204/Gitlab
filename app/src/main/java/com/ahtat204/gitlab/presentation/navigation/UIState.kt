package com.ahtat204.gitlab.presentation.navigation

sealed class UiState {
    data object Loading : UiState()
    data object Authenticated : UiState()
    data object Unauthenticated : UiState()
}