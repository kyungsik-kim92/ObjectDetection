package com.example.presentation.ui.register

sealed class RegisterUiState {
    data object Idle : RegisterUiState()
    data object Loading : RegisterUiState()
    data object Success : RegisterUiState()
}

sealed class RegisterUiEvent {
    data object RouteHome : RegisterUiEvent()
    data class ShowToast(val message: String) : RegisterUiEvent()
}