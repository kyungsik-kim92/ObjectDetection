package com.example.presentation.ui.splash

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object RouteLogin : SplashUiState()
}