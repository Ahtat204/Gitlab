package com.asue24.gitlab
sealed class UiState {
    object Loading : UiState()
    object Authenticated : UiState()
    object Unauthenticated : UiState()
}