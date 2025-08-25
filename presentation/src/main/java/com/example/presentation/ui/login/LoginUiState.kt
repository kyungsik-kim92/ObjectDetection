package com.example.presentation.ui.login


sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
}

sealed class LoginUiEvent {
    data object RouteRegister : LoginUiEvent()
    data object RouteHome : LoginUiEvent()
    data class ShowToast(val message: String) : LoginUiEvent()
}